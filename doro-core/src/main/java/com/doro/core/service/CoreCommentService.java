package com.doro.core.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doro.common.constant.CommentConst;
import com.doro.common.exception.ValidException;
import com.doro.common.response.ResponseResult;
import com.doro.core.utils.UserUtil;
import com.doro.orm.api.CommentService;
import com.doro.orm.bean.CommentBean;
import com.doro.orm.model.request.RequestComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * @author jiage
 */
@Service
public class CoreCommentService {

    private final ThreadPoolTaskExecutor coreTask;
    private final CommentService commentService;

    @Autowired
    public CoreCommentService(ThreadPoolTaskExecutor coreTask, CommentService commentService) {
        this.coreTask = coreTask;
        this.commentService = commentService;
    }

    public boolean save(RequestComment requestComment) {
        valid(requestComment);
        Long userId = UserUtil.getUserId();
        CommentBean commentBean = new CommentBean()
                .setUserId(userId)
                .setPostId(requestComment.getPostId())
                .setContent(requestComment.getContent());

        return commentService.saveComment(commentBean);
    }

    public Page<CommentBean> page(RequestComment requestComment) {
        List<CommentBean> commentList = commentService.page(requestComment);
        Future<Long> countFuture = coreTask.submit(() -> commentService.getPostCommentCount(requestComment));

        if (CollUtil.isNotEmpty(commentList)) {
            List<Long> ids = commentList.stream().map(CommentBean::getId).collect(Collectors.toList());
            System.out.println(commentService.sub(ids));
        }
        Page<CommentBean> page = requestComment.asPage();

        try {
            Long count = countFuture.get(10, TimeUnit.SECONDS);
            page.setRecords(commentList);
            page.setTotal(count);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        return page;
    }
//    public Page<CommentBean> page(RequestComment requestComment) {
//        Future<List<CommentBean>> commentListFuture = coreTask.submit(() -> commentService.page(requestComment));
//        Future<Long> countFuture = coreTask.submit(() -> commentService.getPostCommentCount(requestComment));
//
//        Page<CommentBean> page = requestComment.asPage();
//
//        try {
//            List<CommentBean> commentList = commentListFuture.get(10, TimeUnit.SECONDS);
//            Long count = countFuture.get(10, TimeUnit.SECONDS);
//            page.setRecords(commentList);
//            page.setTotal(count);
//        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//            throw new RuntimeException(e);
//        }
//        return page;
//    }

    public List<CommentBean> pageByIds(RequestComment requestComment) {
        List<Long> ids = commentService.getPageIds(requestComment.getPostId());
        return commentService.pageByIds(ids);
    }


//    public List<CommentBean> getFirstPageCache() {
//        RedisUtil.createList().
//    }
//
//    public List<CommentBean> getPostFirstFewComments(Long postId) {
//        String cacheKey = CacheKey.POST_COMMENTS_PREFIX + postId;
//        RScoredSortedSet<CommentBean> scoredSortedSet = RedisUtil.createSortedset(cacheKey);
//        List<CommentBean> list = (List<CommentBean>) scoredSortedSet.readAll();
//
//        if (CollUtil.isEmpty(list)) {
//            MyLock lock = LockUtil.lock(cacheKey);
//            list = multiCheck.getIfPresent(postId);
//            if (list == null) {
//                System.out.println("oooooooooooooooo");
//                list = commentService.page(postId, CommentConst.NORMAL_PAGE_SIZE);
//                Map<CommentBean, Double> map = new HashMap<>(list.size());
//                for (CommentBean comment : list) {
//                    if (comment.getDel()) {
//                        comment.setContent("已删除");
//                    }
//                    double score = comment.getCreateTime().getTime();
//                    map.put(comment, score);
//                }
//                // TODO 空的也添加
//                scoredSortedSet.addAll(map);
//            }
//            LockUtil.unlock(lock);
//        }
//        return list;
//    }
//
//    public List<CommentBean> getBySize(Long postId, Integer size) {
//        return commentService.page(postId, size);
//    }

    public ResponseResult<?> getByPostId(RequestComment requestComment) {
        // TODO 已删除的评论如何处理
        Page<CommentBean> page = commentService.getAllByPostId(requestComment);

//        List<CommentBean> list = page.getRecords();
//        List<Long> ll = new ArrayList<>();
//        for (int i = 0; i < 10000; i++) {
//            ll.add(583030491303942L);
//        }
//        RedisUtil.createList("woc").range(0, 5);


//        Map<Object, Double> firstLevel = new HashMap<>(list.size());
//        Map<Long, Map<Object, Double>> secondMap = new HashMap<>(list.size());
//        Map<Object, Double> secondLevel = new HashMap<>(list.size());

//        for (CommentBean comment : list) {
//            double score = comment.getCreateTime().getTime();
//            if (comment.getReplyId() == 0) {
//                firstLevel.put(comment, score);
//            } else {
//                secondLevel.put(comment, score);
//            }
//        }
//
//        String firstLevelKey = "first_level:" + requestComment.getPostId();
//        String secondLevelKey = "second_level:" + requestComment.getPostId();
//
//        RBatch batch = RedisUtil.createBatch();
//        batch.getScoredSortedSet(firstLevelKey).addAllAsync(firstLevel);
//        batch.getScoredSortedSet(secondLevelKey).addAllAsync(secondLevel);
//
//        batch.execute();
        return null;
//        if (CollUtil.isNotEmpty(list)) {
//            LinkedList<ResponseComment> result = new LinkedList<>();
//            for (CommentBean comment : list) {
//                if (comment.getDel()) {
//                }
//            }
//        }

    }

    public void valid(RequestComment requestComment) {
        if (requestComment.getPostId() == null) {
            throw new ValidException("没有关联的帖子");
        }

        if (StrUtil.isEmpty(requestComment.getContent()) || requestComment.getContent().length() > CommentConst.MAX_CONTENT_LENGTH) {
            throw new ValidException("请输入评论");
        }
    }
}
