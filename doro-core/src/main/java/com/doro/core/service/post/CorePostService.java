package com.doro.core.service.post;

import cn.hutool.core.util.StrUtil;
import com.doro.api.model.request.RequestPost;
import com.doro.api.orm.PostService;
import com.doro.bean.PostBean;
import com.doro.cache.api.MyLock;
import com.doro.cache.utils.LockUtil;
import com.doro.cache.utils.RedisUtil;
import com.doro.common.constant.CacheKey;
import com.doro.common.constant.CommonConst;
import com.doro.common.constant.LockKey;
import com.doro.common.constant.PostConst;
import com.doro.common.exception.ValidException;
import com.doro.common.model.Page;
import com.doro.core.service.CoreUserLikeService;
import com.doro.core.service.comment.CoreCommentService;
import com.doro.core.service.count.BaseCountService;
import com.doro.core.utils.UserUtil;
import org.redisson.api.RBatch;
import org.redisson.api.RBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
    private final BaseCountService postViewsCount;

    @Autowired
    public CorePostService(CoreUserLikeService coreUserLikeService, CoreCommentService coreCommentService, ThreadPoolTaskExecutor coreTask, PostService postService, @Qualifier(CacheKey.POST_VIEWS_PREFIX) BaseCountService postViewsCount) {
        this.coreUserLikeService = coreUserLikeService;
        this.coreCommentService = coreCommentService;
        this.coreTask = coreTask;
        this.postService = postService;
        this.postViewsCount = postViewsCount;
    }

    public boolean save(RequestPost requestPost) {
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
        return postService.savePost(postBean);
    }

    /**
     * TODO 与浏览量增加区分开来，进入帖子页面后五秒调用增加浏览量方法
     *
     * @param postId
     * @return
     */
    public PostBean getById(Long postId) {
        String cacheKey = CacheKey.POST_PREFIX + postId;

        RBucket<PostBean> bucket = RedisUtil.createBucket(cacheKey);
        PostBean post = bucket.getAndExpire(CommonConst.COMMON_CACHE_DURATION);
        Future<Long> viewsFuture = coreTask.submit(() -> postViewsCount.getCountFromCache(postId));
        if (post == null) {
            // 互斥锁
            MyLock lock = LockUtil.lock(LockKey.INIT_POST_CACHE_PREFIX + postId, CommonConst.COMMON_LOCK_LEASE_SECONDS);
            // 二次检查
            if (!bucket.isExists()) {
                post = postService.getPostById(postId);
                if (post != null) {
                    bucket.set(post, CommonConst.COMMON_CACHE_DURATION);
                }
            }
            LockUtil.unlock(lock);
            return post;
        }

        try {
            Long views = viewsFuture.get();
            if (views != null) {
                post.setViews(views);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        postViewsCount.incrCount(post.getId());
        return post;
    }

    /**
     * 获取缓存中的帖子和浏览量
     *
     * @param cacheKey      帖子缓存 key
     * @param cacheViewsKey 浏览量缓存 key
     * @return 缓存
     */
    private List<?> getPostAndViewsCache(String cacheKey, String cacheViewsKey) {
        RBatch rBatch = RedisUtil.createBatch();
        // 获取帖子缓存
        rBatch.getBucket(cacheKey).getAndExpireAsync(PostConst.CACHE_DURATION);
        // 为浏览量缓存延长超时时间
        rBatch.getAtomicLong(cacheViewsKey).expireIfSetAsync(PostConst.CACHE_VIEWS_DURATION);
        // 获取浏览量
        rBatch.getAtomicLong(cacheViewsKey).getAsync();
        RedisUtil.initBatch(cacheKey);
        return rBatch.execute().getResponses();
    }

    public Page<PostBean> page(RequestPost requestPost) {
        return postService.page(requestPost);
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
