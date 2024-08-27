package com.doro.core.service.post;

import cn.hutool.core.util.StrUtil;
import com.doro.cache.api.MyLock;
import com.doro.cache.utils.LockUtil;
import com.doro.cache.utils.RedisUtil;
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
    private final SectionService sectionService;
    private final PostService postService;

    @Autowired
    public CorePostService(CoreUserLikeService coreUserLikeService, CoreCommentService coreCommentService, ThreadPoolTaskExecutor coreTask, SectionService sectionService, PostService postService) {
        this.coreUserLikeService = coreUserLikeService;
        this.coreCommentService = coreCommentService;
        this.coreTask = coreTask;
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
        String key = "POST:" + postId;
        RBucket<PostBean> rBucket = RedisUtil.createBucket(key);
        PostBean post = rBucket.getAndExpire(Duration.ofMinutes(5));

//        RequestComment requestComment = new RequestComment();
//        requestComment.setPostId(583023745363973L);
//        requestComment.setSize(20);
//        requestComment.setCurrent(1);
//        coreCommentService.getByPostId(requestComment);

        if (post == null) {
            System.out.println("初始化");

            String lockKey = "INIT_POST_VIEW:" + postId;
            MyLock lock = LockUtil.lock(lockKey, 10);
            if (!rBucket.isExists()) {
                System.out.println("only");
                post = postService.getPostById(postId);
                rBucket.setIfAbsent(post, Duration.ofMinutes(5));
            }
            lock.unlock();
        }
        // TODO 多线程?
        if (post != null) {
            post.setViews(incrAndGetViews(postId, post.getViews()));
            return ResponseResult.success(post);
        }
        return ResponseResult.error(MessageEnum.NO_DATA_ERROR);
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
