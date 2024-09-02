package com.doro.core.service.comment;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doro.cache.api.MyLock;
import com.doro.cache.utils.LockUtil;
import com.doro.cache.utils.RedisUtil;
import com.doro.common.constant.CacheKey;
import com.doro.common.constant.CommentConst;
import com.doro.common.exception.ValidException;
import com.doro.core.utils.UserUtil;
import com.doro.api.orm.CommentService;
import com.doro.api.orm.PostService;
import com.doro.api.orm.SubCommentService;
import com.doro.api.bean.CommentBean;
import com.doro.api.model.request.RequestComment;
import org.redisson.api.RList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 评论服务
 * <p>
 * 评论只要保存成功，就无法修改，只能删除，所以不应有修改操作（允许修改的话，其他评论如果引用了该评论，可能会产生歧义）
 *
 * @author jiage
 */
@Service
public class CoreCommentService {

    private final CommentCountService commentCountService;
    private final SubCommentService subCommentService;
    private final ThreadPoolTaskExecutor coreTask;
    private final CommentService commentService;
    private final PostService postService;

    @Autowired
    public CoreCommentService(CommentCountService commentCountService, SubCommentService subCommentService, ThreadPoolTaskExecutor coreTask, CommentService commentService, PostService postService) {
        this.commentCountService = commentCountService;
        this.subCommentService = subCommentService;
        this.coreTask = coreTask;
        this.commentService = commentService;
        this.postService = postService;
    }

    /**
     * 可能会出现的问题：频繁插入，数据库压力过大；插入慢
     * <pre>
     * 批量插入（维护一个集合，将几秒内的数据合在一起插入，可能会出现数据丢失）
     * 先放入缓存（分页查询逻辑复杂，实时性较差）
     * 异步保存（可能会出现丢失的情况）
     * 发送到 MQ（保证消息不丢失，可以起到限流的作用，有一定的延迟）
     * </pre>
     *
     * @param requestComment 请求信息
     * @return 是否保存成功
     */
    public boolean save(RequestComment requestComment) {
//        valid(requestComment);
        Long userId = UserUtil.getUserId();
        CommentBean commentBean = new CommentBean()
                .setUserId(userId)
                .setPostId(requestComment.getPostId())
                .setContent(requestComment.getContent());

        // 不为空为子评论
        if (requestComment.getParentId() != null) {
            commentBean.setParentId(requestComment.getParentId())
                    .setRepliedId(requestComment.getRepliedId())
                    .setRepliedUserId(requestComment.getRepliedUserId());
        }

        if (commentService.saveComment(commentBean)) {
            commentCountService.updateComments(CacheKey.POST_COMMENTS_PREFIX, requestComment.getPostId());
            if (requestComment.getParentId() != null) {
                commentCountService.updateComments(CacheKey.COMMENT_COMMENTS_PREFIX, commentBean.getId());
            }
            return true;
        }

        return false;
    }

//    public boolean deleteById(Long id) {
//        commentService.delById();
//        subCommentService.delById();
//    }

    /**
     * 根据评论获取子评论列表
     *
     * @param commentList
     */
    private void setSubCommentList(List<CommentBean> commentList) {
        if (CollUtil.isNotEmpty(commentList)) {
            Map<Long, CommentBean> idMap = commentList.stream().filter(v -> v.getComments() > 0).collect(Collectors.toMap(CommentBean::getId, Function.identity()));
            if (MapUtil.isNotEmpty(idMap)) {
                // 只查出前面几个评论，
                List<CommentBean> subCommentList = subCommentService.getByParentIds(idMap.keySet(), CommentConst.NORMAL_SUB_COMMENT_COUNT);
                for (CommentBean subComment : subCommentList) {
                    CommentBean commentBean = idMap.get(subComment.getParentId());
                    List<CommentBean> subList = commentBean.getSubList();
                    if (subList == null) {
                        commentBean.setSubList(subList = new ArrayList<>());
                    }
                    subList.add(subComment);
                }
            }
        }
    }

    /**
     * 分页查询
     * <p>
     * 可能会出现的问题：频繁调用，数据库压力过大；深度分页慢
     * <pre>
     * 使用缓存进行分页，将帖子一定数量内的 ID 放入缓存中（排序问题，可能会按时间或热度排序，维护多个列表？被删除的 ID 如何处理？使用 Zset，数据库与缓存的顺序不一样，使用 List，无法保证最后添加的一定是最新的）
     * </pre>
     * <p>
     * 第一页通常是查看量最多的，如果在第一页就显示热度最高的几个评论，甚至很多人都不会去翻页。最后一页的访问量应该也不会很低，大多数人查看的页数一般就在前几页和最后几页，并且一般发送评论成功后都会自动跳到最后一页
     * <p>
     * 所以是否有必要将大量的 ID 列表放入缓存呢？如果数据量过大，别说缓存占用大，就是光从数据库中查询都很耗时，但换句话说，过大的数据量，MySQL 已经不合适了
     *
     * @param requestComment 请求信息
     * @return 分页结果
     */
    public Page<CommentBean> page(RequestComment requestComment) {
        Page<CommentBean> page = pageUseMinId(requestComment);
        if (page == null) {
            page = requestComment.asPage();
            page.setRecords(commentService.page(requestComment));
            page.setTotal(commentCountService.getCommentCount(requestComment.getPostId()));
            setSubCommentList(page.getRecords());
        }
        return page;
    }

    /**
     * 在进行分页查询时使用 id 范围查询（WHERE id >= minId），以提升深度分页的效率，仅在待查询的数据量超过一定范围后生效
     * <pre>
     * 局限性：只能用于同一查询规则的的分页查询，任何条件不同，都只能查询到错误数据，如果增删频繁（不允许修改），可能会查到错误数据，甚至反而影响查询效率
     * 10w 条以内的数据量进行深度分页能在 40ms 左右返回结果，相比之下延迟关联（先查出一个分页范围的 id 列表，再用这个列表去查询）需要 1s 左右，普通分页要 4s 左右
     * 数据量特别大的情况，应当使用 Clickhouse、ElasticSearch 等
     * 每隔 CommentConst.ID_GAP 条记录取一个 id，并将 id 列表放入缓存，间隔应在 100 ~ 500 之间，过小，id 列表就会过大，初始化缓存的效率也会变低（查询数据慢，缓存占用空间大），过大影响查询效率
     * 删除评论时同时删除缓存，添加评论并不会立即删除缓存，如果会出现需要的 minId 不在缓存中的情况，才会删除缓存，必须要保证查询的范围是合法的范围 TODO 范围合法性校验
     * </pre>
     *
     * @param requestComment 请求信息
     * @return 分页结果
     */
    public Page<CommentBean> pageUseMinId(RequestComment requestComment) {
        long postId = requestComment.getPostId();
        int current = requestComment.getCurrent();
        int size = requestComment.getSize();
        current = (current == 0 ? current : current - 1) * size;

        // 低于一定数量不启用
        if (current > CommentConst.USE_MIN_ID_PAGE) {
            int index = current / CommentConst.ID_GAP;

            String cacheKey = CacheKey.COMMENT_MIN_ID_PREFIX + postId;

            List<?> list = RedisUtil.initBatch(cacheKey)
                    .expire(CommentConst.MIN_ID_CACHE_DURATION)
                    .listSize()
                    .listGet(index)
                    .execute();

            int cacheSize = (Integer) list.get(1);
            Long minId = (Long) list.get(2);

            // 要做区分，一种是缓存中真的没有，一种是有缓存但不在范围中（传入的页码可能会大于实际的最大页码数）
            if (cacheSize > 0) {
                if (minId != null) {
                    // 要做好缓存的维护，增删数据要更新缓存，否则会查出错误数据
                    List<CommentBean> records = commentService.pageUseMinId(postId, minId, current % CommentConst.ID_GAP, size);
                    // 添加子评论
                    setSubCommentList(records);

                    Page<CommentBean> page = requestComment.asPage();
                    page.setTotal(commentCountService.getCommentCount(postId));
                    page.setRecords(records);
                    return page;
                }
            }
            // 异步添加缓存
            coreTask.execute(() -> initIdsCache(requestComment.getPostId(), cacheSize));
        }

        return null;
    }

    /**
     * 初始化间隔 id 列表缓存，注意缓存的维护
     *
     * @param postId
     */
    private void initIdsCache(Long postId, Integer cacheSize) {
        String cacheKey = CacheKey.COMMENT_MIN_ID_PREFIX + postId;
        MyLock lock = LockUtil.lock(cacheKey, 10);
        RList<Long> list = RedisUtil.createList(cacheKey);
        int currentCacheSize = list.size();
        // 当前缓存的大小要大于之前记录的缓存大小
        // cacheSize 为 0 表示之前没有缓存
        // cacheSize 大于 0 表示有缓存，但缓存中没有满足的 minId
        // 只要保证查询的范围在合法的范围内（例如只有 100 条数据，要查第 101 条数据），那么经过下面一轮的逻辑，并发的其他线程再查询到的 currentCacheSize 总是大于 cacheSize 的
        if (currentCacheSize <= cacheSize) {
            List<Long> ids = commentService.everyFewId(postId, CommentConst.ID_GAP);
            RedisUtil.initBatch(cacheKey)
                    .delete()
                    .listAddAll(ids)
                    .expire(CommentConst.MIN_ID_CACHE_DURATION)
                    .execute();
        }
        LockUtil.unlock(lock);
    }

    /**
     * 删除间隔 id 列表缓存
     *
     * @param postId
     */
    private void delIdsCache(Long postId) {
        String cacheKey = CacheKey.COMMENT_MIN_ID_PREFIX + postId;
        RList<Long> list = RedisUtil.createList(cacheKey);
        list.delete();
    }

    public void valid(RequestComment requestComment) {
        // TODO 帖子是否允许评论
//        if (requestComment.getPostId() == null) {
//            throw new ValidException("没有关联的帖子");
//        }

        if (StrUtil.isEmpty(requestComment.getContent()) || requestComment.getContent().length() > CommentConst.MAX_CONTENT_LENGTH) {
            throw new ValidException("请输入评论");
        }
    }
}
