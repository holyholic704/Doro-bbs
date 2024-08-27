package com.doro.core.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doro.cache.api.MyLock;
import com.doro.cache.utils.LockUtil;
import com.doro.cache.utils.RedisUtil;
import com.doro.common.constant.CommentConst;
import com.doro.common.enumeration.MessageEnum;
import com.doro.common.exception.ValidException;
import com.doro.common.response.ResponseResult;
import com.doro.core.utils.UserUtil;
import com.doro.orm.api.CommentService;
import com.doro.orm.bean.CommentBean;
import com.doro.orm.model.request.RequestComment;
import org.redisson.api.RScoredSortedSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiage
 */
@Service
public class CoreCommentService {

    private final CommentService commentService;

    @Autowired
    public CoreCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    public ResponseResult<?> save(RequestComment requestComment) {
        valid(requestComment);
        Long userId = UserUtil.getUserId();
        CommentBean commentBean = new CommentBean()
                .setUserId(userId)
                .setPostId(requestComment.getPostId())
                .setReplyId(requestComment.getReplyId())
                .setContent(requestComment.getContent());

        return commentService.saveComment(commentBean) ? ResponseResult.success(MessageEnum.SAVE_SUCCESS) : ResponseResult.error(MessageEnum.SAVE_ERROR);
    }

    public List<CommentBean> getPostFirstFewComment(Long postId) {
        String key = "POST_COMMENTS:" + postId;
        RScoredSortedSet<CommentBean> scoredSortedSet = RedisUtil.createSortedset(key);
        List<CommentBean> list = (List<CommentBean>) scoredSortedSet.readAll();
        if (CollUtil.isEmpty(list)) {
            MyLock lock = LockUtil.lock(key);
            System.out.println("oooooooooooooooo");
            list = commentService.getBySize(postId, CommentConst.NORMAL_PAGE_SIZE);
            Map<CommentBean, Double> map = new HashMap<>(list.size());
            for (CommentBean comment : list) {
                if (comment.getDel()) {
                    comment.setContent("已删除");
                }
                double score = comment.getCreateTime().getTime();
                map.put(comment, score);
            }
            // TODO 空的也添加
            scoredSortedSet.addAll(map);
            lock.unlock();
        }
        return list;
    }

    public List<CommentBean> getBySize(Long postId, Integer size) {
        return commentService.getBySize(postId, size);
    }

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
