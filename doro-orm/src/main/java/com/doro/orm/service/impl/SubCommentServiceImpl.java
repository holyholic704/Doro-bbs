package com.doro.orm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.api.orm.SubCommentService;
import com.doro.bean.SubCommentBean;
import com.doro.orm.mapper.SubCommentMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 子评论
 *
 * @author jiage
 */
@Service
class SubCommentServiceImpl extends ServiceImpl<SubCommentMapper, SubCommentBean> implements SubCommentService {

    @Override
    public List<SubCommentBean> getByParentIds(Collection<Long> ids, int subCommentCount) {
        return this.getBaseMapper().getByParentIds(ids, subCommentCount);
    }

    @Override
    public boolean delById(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean saveSubComment(SubCommentBean subCommentBean) {
        return this.save(subCommentBean);
    }
}
