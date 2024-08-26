package com.doro.core.service.post;

import cn.hutool.core.util.StrUtil;
import com.doro.cache.api.MyLock;
import com.doro.cache.utils.LockUtil;
import com.doro.cache.utils.RedisUtil;
import com.doro.cache.utils.RemoteCacheUtil;
import com.doro.common.constant.PostConst;
import com.doro.common.enumeration.MessageEnum;
import com.doro.common.exception.ValidException;
import com.doro.common.response.ResponseResult;
import com.doro.core.service.CoreUserPostLikeService;
import com.doro.core.utils.UserUtil;
import com.doro.orm.api.PostService;
import com.doro.orm.api.SectionService;
import com.doro.orm.bean.PostBean;
import com.doro.orm.model.request.RequestPost;
import org.redisson.api.RBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 帖子服务
 *
 * @author jiage
 */
@Service
public class CorePostService {

    private final CoreUserPostLikeService coreUserPostLikeService;
    private final SectionService sectionService;
    private final PostService postService;

    @Autowired
    public CorePostService(CoreUserPostLikeService coreUserPostLikeService, SectionService sectionService, PostService postService) {
        this.coreUserPostLikeService = coreUserPostLikeService;
        this.sectionService = sectionService;
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

    public ResponseResult<?> getById(Long postId) {
        // TODO 浏览量，在一定时间内同一用户或同一IP多次获取同一帖子，不增加浏览量
        PostBean post = RemoteCacheUtil.get("POST:" + postId);
        if (post == null) {
            post = postService.getPostById(postId);
            RemoteCacheUtil.putIfAbsent("POST:" + postId, post);
        }
        // TODO 多线程?
        if (post != null) {
            post.setViews(incrAndGetViews(postId, post.getViews()));
            return ResponseResult.success(post);
        }
        return ResponseResult.error(MessageEnum.NO_DATA_ERROR);
    }

    private Long incrAndGetViews(Long postId, Long views) {
        String lockKey = "INIT_POST_VIEW:" + postId;
        MyLock lock = LockUtil.tryLock(lockKey, 5, 5);
        views = incrViews(postId, views);
        lock.unlock();
        return views;
    }

    private Long incrViews(Long postId, Long views) {
        String key = "POST_VIEW:" + postId;
        // 不使用自增：无法同时设置值与超时时间，需使用批量操作
        // 不使用 CAS 操作：分布式锁确保了同时只有一个线程能对浏览量进行修改
        // 不使用 getex 每次访问 key 自动延长时间：key 不存在无法延长
        RBucket<Long> bucket = RedisUtil.createBucket(key);

        Long cacheViews = bucket.get();

        // 如果没有缓存就使用数据库中的值
        // 不用担心执行到这，key 正好过期
        // TODO 键过期自动刷新到数据库？定时任务？
        views = cacheViews == null ? (views + 1) : (cacheViews + 1);
        // 每次更新重新设置超时时间
        bucket.set(views, Duration.ofMinutes(5));
        return views;
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
