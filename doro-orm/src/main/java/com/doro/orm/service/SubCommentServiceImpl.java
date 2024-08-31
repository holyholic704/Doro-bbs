package com.doro.orm.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.orm.api.SubCommentService;
import com.doro.orm.bean.CommentBean;
import com.doro.orm.bean.SubCommentBean;
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
    public List<CommentBean> getByParentIds(Collection<Long> ids, int subCommentCount) {
        return this.getBaseMapper().getByParentIds(ids, subCommentCount);
    }

}
