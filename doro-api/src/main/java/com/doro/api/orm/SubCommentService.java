package com.doro.api.orm;

import com.doro.bean.CommentBean;

import java.util.Collection;
import java.util.List;

/**
 * @author jiage
 */
public interface SubCommentService {

    List<CommentBean> getByParentIds(Collection<Long> ids, int subCommentCount);

    boolean delById(Long id);
}
