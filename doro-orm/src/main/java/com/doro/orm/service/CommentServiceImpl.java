package com.doro.orm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.orm.api.CommentService;
import com.doro.orm.bean.CommentBean;
import com.doro.orm.mapper.CommentMapper;
import com.doro.orm.model.request.RequestComment;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
    public Page<CommentBean> getAllByPostId(RequestComment requestComment) {
        return this.page(requestComment.asPage(), new LambdaQueryWrapper<CommentBean>()
                .select(CommentBean::getId, CommentBean::getUserId, CommentBean::getContent, CommentBean::getCreateTime, CommentBean::getDel)
                .eq(CommentBean::getPostId, requestComment.getPostId())
                .orderByAsc(CommentBean::getCreateTime));
    }

    @Override
    public List<CommentBean> page(RequestComment requestComment) {
        int current = requestComment.getCurrent();
        int size = requestComment.getSize();
        int from = (current == 0 ? current : current - 1) * size;
        return this.getBaseMapper().page(requestComment.getPostId(), from, size);
    }

    @Override
    public List<CommentBean> pageByIds(List<Long> ids) {
//        return this.getBaseMapper().pageByIds(ids);
        return this.list(new LambdaQueryWrapper<CommentBean>()
                .in(CommentBean::getId, ids)
                .orderByAsc(CommentBean::getCreateTime));
    }

    @Override
    public List<CommentBean> getPageIds(Long postId) {
        return this.list(new LambdaQueryWrapper<CommentBean>()
                .select(CommentBean::getId, CommentBean::getCreateTime)
                .eq(CommentBean::getPostId, postId)
                .eq(CommentBean::getParentId, 0));
    }

    public long getPostCommentIdList(long postId) {
        return this.count(new LambdaQueryWrapper<CommentBean>()
                .eq(CommentBean::getPostId, postId));
    }

    public boolean delById(Long id) {
        return this.removeById(id);
    }

    @Override
    public long getPostCommentCount(RequestComment requestComment) {
        return this.count(new LambdaQueryWrapper<CommentBean>()
                .eq(CommentBean::getPostId, requestComment.getPostId()));
    }

    @Override
    public List<Long> everyFewId(Long postId) {
        return this.getBaseMapper().everyFewId(postId);
    }

    @Override
    public List<CommentBean> pageUseMinId(long postId, long minId, int current, int size) {
        return this.getBaseMapper().pageUseMinId(postId, minId, current, size);
    }

    @Override
    public List<CommentBean> subCommentList(Collection<Long> ids) {
        return this.getBaseMapper().subCommentList(ids);
    }

//    public boolean updateComments(Long id) {
//        this.update();
//    }
}
