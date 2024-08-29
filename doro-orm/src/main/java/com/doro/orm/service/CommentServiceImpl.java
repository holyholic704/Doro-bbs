package com.doro.orm.service;

import cn.hutool.core.collection.CollUtil;
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
import java.util.stream.Collectors;

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
                .select(CommentBean::getId, CommentBean::getUserId, CommentBean::getContent, CommentBean::getCreateTime, CommentBean::getDel)
                .eq(CommentBean::getPostId, requestComment.getPostId())
                .orderByAsc(CommentBean::getCreateTime));
    }

    @Override
    public Page<CommentBean> page(RequestComment requestComment) {
        return this.page(requestComment.asPage(), new LambdaQueryWrapper<CommentBean>()
                .eq(CommentBean::getPostId, requestComment.getPostId())
                .eq(CommentBean::getParentId, 0)
                .orderByAsc(CommentBean::getCreateTime)
        );
//        int current = requestComment.getCurrent();
//        int size = requestComment.getSize();
//        int from = (current == 0 ? current : current - 1) * size;
//        return this.getBaseMapper().page(requestComment.getPostId(), from, size);
    }

    @Override
    public List<CommentBean> pageByIds(List<Long> ids) {
//        return this.getBaseMapper().pageByIds(ids);
        return this.list(new LambdaQueryWrapper<CommentBean>()
                .in(CommentBean::getId, ids)
                .orderByAsc(CommentBean::getCreateTime));
    }

    @Override
    public List<Long> getPageIds(Long postId) {
        List<CommentBean> commentList = this.list(new LambdaQueryWrapper<CommentBean>()
                .select(CommentBean::getId, CommentBean::getCreateTime)
                .eq(CommentBean::getPostId, postId)
                .eq(CommentBean::getParentId, 0));

        if (CollUtil.isNotEmpty(commentList)) {
            return commentList.stream().map(CommentBean::getId).collect(Collectors.toList());
        }
        return null;
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
    public List<CommentBean> subCommentList(Collection<Long> ids) {
        return this.getBaseMapper().subCommentList(ids);
    }

//    public boolean updateComments(Long id) {
//        this.update();
//    }
}
