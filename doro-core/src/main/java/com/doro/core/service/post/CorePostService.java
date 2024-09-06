package com.doro.core.service.post;

import cn.hutool.core.util.StrUtil;
import com.doro.api.model.request.RequestPost;
import com.doro.api.orm.PostService;
import com.doro.bean.PostBean;
import com.doro.cache.utils.RedisUtil;
import com.doro.common.constant.CacheKey;
import com.doro.common.constant.CommonConst;
import com.doro.common.constant.PostConst;
import com.doro.common.exception.ValidException;
import com.doro.common.model.Page;
import com.doro.core.service.count.BaseCountService;
import com.doro.core.utils.UserUtil;
import org.redisson.api.RBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 帖子服务
 *
 * @author jiage
 */
@Service
public class CorePostService {

    private final ThreadPoolTaskExecutor coreIoTask;
    private final PostService postService;
    private final BaseCountService postViewsCount;

    @Autowired
    public CorePostService(ThreadPoolTaskExecutor coreIoTask, PostService postService, @Qualifier(CacheKey.POST_VIEWS_PREFIX) BaseCountService postViewsCount) {
        this.coreIoTask = coreIoTask;
        this.postService = postService;
        this.postViewsCount = postViewsCount;
    }

    public boolean save(RequestPost requestPost) {
        valid(requestPost);
        // 可以认为一定能获取到用户 ID
        Long authorId = UserUtil.getUserId();
        String authorName = UserUtil.getUsername();
        PostBean postBean = new PostBean()
                .setTitle(requestPost.getTitle())
                .setContent(requestPost.getContent())
                .setAuthorId(authorId)
                .setAuthorName(authorName)
                .setSectionId(requestPost.getSectionId())
                .setActivated(false);
        // TODO 发帖权限，等级
        // TODO 是否需要审核，审核规则？不审核、手动审核、敏感词过滤，谁来审核，版主？管理员？坛主？
        // TODO 防灌水
        // TODO 写入优化
        return postService.savePost(postBean);
    }

    public boolean update(RequestPost requestPost) {
        valid(requestPost);
        PostBean postBean = new PostBean()
                .setTitle(requestPost.getTitle())
                .setContent(requestPost.getContent())
                .setActivated(false);
        return postService.updatePost(postBean);
    }

    /**
     * 获取帖子详情
     *
     * @param postId 帖子 id
     * @return
     */
    public PostBean getById(final Long postId) {
        // 异步获取帖子的访问量
        Future<Long> viewsFuture = coreIoTask.submit(() -> postViewsCount.getCountFromCache(postId));

        // 先尝试从缓存中获取
        String cacheKey = CacheKey.POST_PREFIX + postId;
        RBucket<PostBean> bucket = RedisUtil.createBucket(cacheKey);
        Duration cacheDuration = CommonConst.COMMON_CACHE_DURATION;
        PostBean post = bucket.getAndExpire(cacheDuration);

        if (post == null) {
            // 对于一致性要求不高的数据，使用本地锁，虽然不同机器仍会多次执行，但可减少使用分布式锁的网络开销
            synchronized (cacheKey.intern()) {
                post = bucket.getAndExpire(cacheDuration);
                if (post == null) {
                    post = postService.getPostById(postId);
                    // 暂时不做缓存的大小限制，Redis 字符串的缓存在 3kb/千字，做好保存时的长度限制即可
                    if (post != null) {
                        bucket.set(post, cacheDuration);
                    }
                }
            }
        }

        if (post != null) {
            try {
                Long views = viewsFuture.get();
                if (views != null) {
                    post.setViews(views);
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        return post;
    }

    public Page<PostBean> page(RequestPost requestPost) {
        Page<PostBean> page = requestPost.asPage();
        page.setRecords(postService.page(requestPost));
        page.setTotal(999);
        return page;
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
