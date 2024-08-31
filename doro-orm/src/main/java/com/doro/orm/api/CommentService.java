package com.doro.orm.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doro.orm.bean.CommentBean;
import com.doro.orm.model.request.RequestComment;

import java.util.Collection;
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

    List<CommentBean> page(RequestComment requestComment);

    List<Long> everyFewId(Long postId,int idGap);

    List<CommentBean> pageUseMinId(long postId, long minId, int current, int size);

}
