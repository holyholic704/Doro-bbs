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

    List<CommentBean> getBySize(long postId, int size);
}
