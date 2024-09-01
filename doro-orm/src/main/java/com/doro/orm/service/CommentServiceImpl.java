package com.doro.orm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
public class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentBean> implements CommentService {

    @Override
    public boolean saveComment(CommentBean commentBean) {
        return this.save(commentBean);
    }

    @Override
    public List<CommentBean> page(RequestComment requestComment) {
        int current = requestComment.getCurrent();
        int size = requestComment.getSize();
        int from = (current == 0 ? current : current - 1) * size;
        return this.getBaseMapper().page(requestComment.getPostId(), from, size);
    }

    public boolean updateComments(long id) {
        return this.getBaseMapper().updateComments(id);
    }

    public boolean delById(Long id) {
        return this.removeById(id);
    }

    @Override
    public List<Long> everyFewId(Long postId, int idGap) {
        return this.getBaseMapper().everyFewId(postId, idGap);
    }

    @Override
    public List<CommentBean> pageUseMinId(long postId, long minId, int current, int size) {
        return this.getBaseMapper().pageUseMinId(postId, minId, current, size);
    }

    @Override
    public long getComments(Long id) {
        CommentBean commentBean = this.getOne(new LambdaQueryWrapper<CommentBean>()
                .select(CommentBean::getComments)
                .eq(CommentBean::getId, id));
        return commentBean != null ? commentBean.getComments() : 0;
    }
}
