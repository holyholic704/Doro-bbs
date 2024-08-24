package com.doro.orm.api;

import com.doro.orm.bean.CommentBean;

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
}
