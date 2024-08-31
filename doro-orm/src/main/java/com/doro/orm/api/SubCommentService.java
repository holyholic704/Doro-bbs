package com.doro.orm.api;

import com.doro.orm.bean.CommentBean;

import java.util.Collection;
import java.util.List;

/**
 * @author jiage
 */
public interface SubCommentService {

    List<CommentBean> getByParentIds(Collection<Long> ids, int subCommentCount);
}
