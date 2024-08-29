package com.doro.orm.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doro.orm.bean.CommentBean;
import com.doro.orm.model.request.RequestComment;

import java.util.List;

/**
 * 评论
 *
 * @author jiage
 */
public interface CommentService {

    /**
     * 保存一个评论
     */
    boolean saveComment(CommentBean commentBean);

    Page<CommentBean> getAllByPostId(RequestComment requestComment);

    List<CommentBean> page(RequestComment requestComment);

    List<CommentBean> pageByIds(List<Long> ids);

    List<Long> getPageIds(Long postId);

    long getPostCommentCount(RequestComment requestComment);

    List<CommentBean> sub(List<Long> ids);
}
