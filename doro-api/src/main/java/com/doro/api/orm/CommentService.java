package com.doro.api.orm;

import com.doro.api.model.request.RequestComment;
import com.doro.bean.CommentBean;

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

    List<Long> everyFewId(Long postId, int idGap);

    List<CommentBean> pageUseMinId(long postId, long minId, int current, int size);

    Long getComments(Long id);

    boolean updateComments(long id);

    boolean delById(Long id);

    boolean saveBatchComment(List<CommentBean> list);
}
