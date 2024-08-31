package com.doro.core.service;

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
import com.doro.orm.api.CommentService;
import com.doro.orm.api.SubCommentService;
import com.doro.orm.bean.CommentBean;
import com.doro.orm.model.request.RequestComment;
import org.redisson.api.RBatch;
import org.redisson.api.RList;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Duration;
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

    private final SubCommentService subCommentService;
    private final ThreadPoolTaskExecutor coreTask;
    private final CommentService commentService;

    @Autowired
    public CoreCommentService(SubCommentService subCommentService, ThreadPoolTaskExecutor coreTask, CommentService commentService) {
        this.subCommentService = subCommentService;
        this.coreTask = coreTask;
        this.commentService = commentService;
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

        return commentService.saveComment(commentBean);
    }

    private void incrCacheComments(Long postId, Long parentId) {
        String cacheKey = CacheKey.POST_COMMENTS_PREFIX + postId;
        Duration duration = Duration.ofMinutes(5);
        RBatch batch = RedisUtil.createBatch();
        batch.getMap(cacheKey, StringCodec.INSTANCE).expireAsync(duration);
        batch.getMap(cacheKey, StringCodec.INSTANCE).addAndGetAsync("COMMENTS", 1);
        batch.getMap(cacheKey, StringCodec.INSTANCE).addAndGetAsync("583030340190213L", 1);
    }

    private void setSubCommentList(List<CommentBean> commentList) {
        if (CollUtil.isNotEmpty(commentList)) {
            Map<Long, CommentBean> idMap = commentList.stream().filter(v -> v.getComments() > 0).collect(Collectors.toMap(CommentBean::getId, Function.identity()));
            if (MapUtil.isNotEmpty(idMap)) {
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
            page.setTotal(1);
            setSubCommentList(page.getRecords());
        }
        return page;
    }

    /**
     * 在进行分页查询时使用 id 范围查询（WHERE id >= minId），以提升深度分页的效率，仅在待查询的数据量超过一定范围后生效
     * <pre>
     * 局限性：只能用于同一查询规则的的分页查询，任何条件不同，都只能查询到错误数据
     * 10w 条以内的数据量进行深度分页能在 40ms 左右返回结果，相比之下延迟关联（先查出一个分页范围的 id 列表，再用这个列表去查询）需要 1s 左右，普通分页要 4s 左右
     * 数据量特别大的情况，应当使用 Clickhouse、ElasticSearch 等
     * 每隔 CommentConst.ID_GAP 条记录取一个 id，并将 id 列表放入缓存，间隔应在 100 ~ 500 之间，过小，id 列表就会过大，初始化缓存的效率也会变低（查询数据慢，缓存占用空间大），过大影响查询效率
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

            RBatch batch = RedisUtil.createBatch();
            batch.getList(cacheKey).expireAsync(CommentConst.MIN_ID_CACHE_DURATION);
            batch.getList(cacheKey).isExistsAsync();
            batch.getList(cacheKey).getAsync(index);
            List<?> list = batch.execute().getResponses();

            Boolean isExist = (Boolean) list.get(1);
            Long minId = (Long) list.get(2);

            // 要做区分，一种是缓存中真的没有，一种是有缓存但不在范围中（传入的页码可能会大于实际的最大页码数）
            if (isExist != null && isExist) {
                // 要做好缓存的维护，增删数据要更新缓存，否则会查出错误数据
                Page<CommentBean> page = requestComment.asPage();
                if (minId != null) {
                    List<CommentBean> records = commentService.pageUseMinId(postId, minId, current % CommentConst.ID_GAP, size);
                    // TODO 总数
                    page.setTotal(1);
                    page.setRecords(records);
                    // 添加子评论
                    setSubCommentList(page.getRecords());
                }
                return page;
            } else {
                // 异步添加缓存
                coreTask.execute(() -> initIdsCache(requestComment.getPostId()));
            }
        }

        return null;
    }

    /**
     * 初始化间隔 id 列表缓存，注意缓存的维护
     *
     * @param postId
     */
    private void initIdsCache(Long postId) {
        String cacheKey = CacheKey.COMMENT_MIN_ID_PREFIX + postId;
        MyLock lock = LockUtil.lock(cacheKey, 10);
        RList<Long> list = RedisUtil.createList(cacheKey);
        if (!list.isExists()) {
            List<Long> ids = commentService.everyFewId(postId, CommentConst.ID_GAP);
            RBatch batch = RedisUtil.createBatch();
            batch.getList(cacheKey).addAllAsync(ids);
            batch.getList(cacheKey).expireAsync(CommentConst.MIN_ID_CACHE_DURATION);
            batch.execute();
        }
        LockUtil.unlock(lock);
    }

    /**
     * @param postId
     */
    private void updateIdsCache(Long postId) {
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
