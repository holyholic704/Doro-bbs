package com.doro.core.service.post;

import cn.hutool.core.util.StrUtil;
import com.doro.cache.api.MyLock;
import com.doro.cache.utils.LockUtil;
import com.doro.cache.utils.RedisUtil;
import com.doro.common.constant.CacheKey;
import com.doro.common.constant.LockKey;
import com.doro.common.constant.PostConst;
import com.doro.common.enumeration.MessageEnum;
import com.doro.common.exception.ValidException;
import com.doro.common.response.ResponseResult;
import com.doro.core.service.CoreCommentService;
import com.doro.core.service.CoreUserLikeService;
import com.doro.core.utils.UserUtil;
import com.doro.orm.api.PostService;
import com.doro.orm.api.SectionService;
import com.doro.orm.bean.PostBean;
import com.doro.orm.model.request.RequestPost;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBatch;
import org.redisson.api.RBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

/**
 * 帖子服务
 *
 * @author jiage
 */
@Service
public class CorePostService {

    private final CoreUserLikeService coreUserLikeService;
    private final CoreCommentService coreCommentService;
    private final ThreadPoolTaskExecutor coreTask;
    private final PostService postService;

    @Autowired
    public CorePostService(CoreUserLikeService coreUserLikeService, CoreCommentService coreCommentService, ThreadPoolTaskExecutor coreTask, PostService postService) {
        this.coreUserLikeService = coreUserLikeService;
        this.coreCommentService = coreCommentService;
        this.coreTask = coreTask;
        this.postService = postService;
    }

    public ResponseResult<?> save(RequestPost requestPost) {
        valid(requestPost);
        // 可以认为一定能获取到用户 ID
        Long authorId = UserUtil.getUserId();
        PostBean postBean = new PostBean()
                .setTitle(requestPost.getTitle())
                .setContent(requestPost.getContent())
                .setAuthorId(authorId)
                .setSectionId(requestPost.getSectionId())
                .setActivated(false);
        // TODO 发帖权限，等级
        // TODO 是否需要审核，审核规则？不审核、手动审核、敏感词过滤，谁来审核，版主？管理员？坛主？
        // TODO 字数限制
        // TODO 添加缓存，字数限制
        // TODO 防灌水
        return postService.savePost(postBean) ? ResponseResult.success(MessageEnum.SAVE_SUCCESS) : ResponseResult.error(MessageEnum.SAVE_ERROR);
    }

    /**
     * TODO 与浏览量增加区分开来，进入帖子页面后五秒调用增加浏览量方法
     *
     * @param postId
     * @return
     */
    public ResponseResult<?> getById(Long postId) {
        String cacheKey = CacheKey.POST_PREFIX + postId;
        String cacheViewsKey = CacheKey.POST_VIEWS_PREFIX + postId;

        // 异步添加帖子的前几个评论到缓存
        coreTask.execute(() -> coreCommentService.getPostFirstFewComment(postId));

        List<?> postAndViewsCache = getPostAndViewsCache(cacheKey, cacheViewsKey);

        // 获取帖子的缓存
        PostBean post = (PostBean) postAndViewsCache.get(0);

        if (post == null) {
            // 互斥锁
            String lockKey = LockKey.INIT_POST_CACHE_PREFIX + postId;
            RBucket<PostBean> bucket = RedisUtil.createBucket(cacheKey);
            MyLock lock = LockUtil.lock(lockKey, 10);
            if (!bucket.isExists()) {
                System.out.println("only");
                post = postService.getPostById(postId);
                bucket.setIfAbsent(post, Duration.ofMinutes(5));
            }
            LockUtil.unlock(lock);
        }
        if (post != null) {
            // 获取浏览量的缓存
            Long views = (Long) postAndViewsCache.get(2);
            if (views != null && views > 0) {
                post.setViews(views);
            }
            return ResponseResult.success(post);
        }
        return ResponseResult.error(MessageEnum.NO_DATA_ERROR);
    }

    private List<?> getPostAndViewsCache(String cacheKey, String cacheViewsKey) {
        RBatch rBatch = RedisUtil.createBatch();
        // 获取帖子缓存
        rBatch.getBucket(cacheKey).getAndExpireAsync(PostConst.CACHE_DURATION);
        // 为浏览量缓存延长超时时间
        rBatch.getAtomicLong(cacheViewsKey).expireIfSetAsync(PostConst.CACHE_VIEWS_DURATION);
        // 获取浏览量
        rBatch.getAtomicLong(cacheViewsKey).getAsync();
        return rBatch.execute().getResponses();
    }

    private Long getCacheViews(Long postId) {
        String key = "POST_VIEW:" + postId;
        Duration duration = Duration.ofMinutes(5);

        RBatch rBatch = RedisUtil.createBatch();
        rBatch.getAtomicLong(key).expireIfSetAsync(duration);
        rBatch.getAtomicLong(key).getAsync();
        List<?> result = rBatch.execute().getResponses();
        return (Long) result.get(1);
    }

    private Long incrAndGetViews(Long postId, Long views) {
        // TODO 键过期自动刷新到数据库？定时任务？
        String key = "POST_VIEW:" + postId;
        RAtomicLong atomicLong = RedisUtil.createAtomicLong(key);

        Duration duration = Duration.ofMinutes(5);

        RBatch rBatch = RedisUtil.createBatch();
        rBatch.getAtomicLong(key).expireIfSetAsync(duration);
        rBatch.getAtomicLong(key).getAsync();
        List<?> result = rBatch.execute().getResponses();

        Long cacheViews = (Long) result.get(1);

        if (cacheViews != null && cacheViews == 0) {
            rBatch = RedisUtil.createBatch();
            rBatch.getAtomicLong(key).compareAndSetAsync(0, views + 1);
            rBatch.getAtomicLong(key).expireIfNotSetAsync(duration);
            result = rBatch.execute().getResponses();
            Boolean isSuccess = (Boolean) result.get(0);
            if (isSuccess) {
                return views + 1;
            }
        }
        return atomicLong.incrementAndGet();
    }

    public ResponseResult<?> page(RequestPost requestPost) {
        return ResponseResult.success(postService.page(requestPost));
    }

    private void valid(RequestPost requestPost) {
        // TODO 帖子校验规则
        // TODO 标签暂时非必须
        if (StrUtil.isEmpty(requestPost.getTitle()) || requestPost.getTitle().length() < PostConst.MIN_TITLE_LENGTH || requestPost.getTitle().length() > PostConst.MAX_TITLE_LENGTH) {
            throw new ValidException(String.format("请输入标题（%s ~ %s 个字）", PostConst.MIN_TITLE_LENGTH, PostConst.MAX_TITLE_LENGTH));
        }

        if (requestPost.getContent() == null || requestPost.getContent().length() < PostConst.MIN_CONTENT_LENGTH || requestPost.getContent().length() > PostConst.MAX_CONTENT_LENGTH) {
            throw new ValidException(String.format("请输入正文（%s ~ %s 个字）", PostConst.MIN_CONTENT_LENGTH, PostConst.MAX_CONTENT_LENGTH));
        }

        if (requestPost.getSectionId() == null) {
            throw new ValidException("请选择发帖的版块");
        }
    }

}
