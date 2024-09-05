package com.doro.api.orm;

import com.doro.bean.SubCommentBean;

import java.util.Collection;
import java.util.List;

/**
 * @author jiage
 */
public interface SubCommentService {

    List<SubCommentBean> getByParentIds(Collection<Long> ids, int subCommentCount);

    boolean delById(Long id);

    boolean saveSubComment(SubCommentBean subCommentBean);
}
