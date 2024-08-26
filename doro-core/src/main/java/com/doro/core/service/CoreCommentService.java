package com.doro.core.service;

import cn.hutool.core.util.StrUtil;
import com.doro.common.constant.CommentConst;
import com.doro.common.enumeration.MessageEnum;
import com.doro.common.exception.ValidException;
import com.doro.common.response.ResponseResult;
import com.doro.core.utils.UserUtil;
import com.doro.orm.api.CommentService;
import com.doro.orm.bean.CommentBean;
import com.doro.orm.model.request.RequestComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void valid(RequestComment requestComment) {
        if (requestComment.getPostId() == null) {
            throw new ValidException("没有关联的帖子");
        }

        if (StrUtil.isEmpty(requestComment.getContent()) || requestComment.getContent().length() > CommentConst.MAX_CONTENT_LENGTH) {
            throw new ValidException("请输入评论");
        }
    }
}
