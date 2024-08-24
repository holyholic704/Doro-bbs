package com.doro.orm.api;

import com.doro.orm.bean.CommentBean;

/**
 * 评论
 *
 * @author jiage
 */
public interface CommentService {

    boolean saveComment(CommentBean commentBean);
}
