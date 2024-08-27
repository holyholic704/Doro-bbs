package com.doro.orm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.orm.api.CommentService;
import com.doro.orm.bean.CommentBean;
import com.doro.orm.mapper.CommentMapper;
import com.doro.orm.model.request.RequestComment;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论
 *
 * @author jiage
 */
@Service
class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentBean> implements CommentService {

    @Override
    public boolean saveComment(CommentBean commentBean) {
        return this.save(commentBean);
    }

    @Override
    public Page<CommentBean> getAllByPostId(RequestComment requestComment) {
        return this.page(requestComment.asPage(), new LambdaQueryWrapper<CommentBean>()
                .select(CommentBean::getId, CommentBean::getUserId, CommentBean::getReplyId, CommentBean::getContent, CommentBean::getCreateTime, CommentBean::getDel)
                .eq(CommentBean::getPostId, requestComment.getPostId())
                .eq(CommentBean::getReplyId, 0)
                .orderByAsc(CommentBean::getCreateTime));
    }

    @Override
    public List<CommentBean> getBySize(long postId, int size) {
        return this.getBaseMapper().getBySize(postId, size);
    }
}
