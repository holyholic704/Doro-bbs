package com.doro.core.service.post;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doro.cache.api.MyLock;
import com.doro.cache.utils.LockUtil;
import com.doro.cache.utils.RedisUtil;
import com.doro.common.constant.CacheKey;
import com.doro.common.constant.LockKey;
import com.doro.common.constant.PostConst;
import com.doro.common.exception.ValidException;
import com.doro.core.service.CoreCommentService;
import com.doro.core.service.CoreUserLikeService;
import com.doro.core.utils.UserUtil;
import com.doro.orm.api.PostService;
import com.doro.orm.bean.PostBean;
import com.doro.orm.model.request.RequestPost;
import org.redisson.api.RBatch;
import org.redisson.api.RBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

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
        String cacheViewsKey = CacheKey.POST_VIEWS_PREFIX + postId;

        // 尝试从缓存中获取帖子和浏览量
        List<?> postAndViewsCache = getPostAndViewsCache(cacheKey, cacheViewsKey);

        // 获取帖子的缓存
        PostBean post = (PostBean) postAndViewsCache.get(0);

        if (post == null) {
            // 互斥锁
            String lockKey = LockKey.INIT_POST_CACHE_PREFIX + postId;
            MyLock lock = LockUtil.lock(lockKey, 10);

            RBucket<PostBean> bucket = RedisUtil.createBucket(cacheKey);
            // 二次检查
            if (!bucket.isExists()) {
                post = postService.getPostById(postId);
                if (post != null) {
                    bucket.setIfAbsent(post, PostConst.CACHE_DURATION);
                }
            }
            LockUtil.unlock(lock);
        }

        if (post != null) {
            // 获取浏览量的缓存
            Long views = (Long) postAndViewsCache.get(2);
            if (views != null && views > 0) {
                post.setViews(views);
            }
            return post;
        }
        return null;
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
