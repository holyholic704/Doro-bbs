package com.doro.orm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.api.model.request.RequestPost;
import com.doro.api.orm.PostService;
import com.doro.bean.PostBean;
import com.doro.common.model.Page;
import com.doro.orm.mapper.PostMapper;
import org.springframework.stereotype.Service;

/**
 * 帖子
 *
 * @author jiage
 */
@Service
class PostServiceImpl extends ServiceImpl<PostMapper, PostBean> implements PostService {

    @Override
    public PostBean getPostById(Long postId) {
        return this.getById(postId);
    }

    @Override
    public boolean savePost(PostBean postBean) {
        return this.save(postBean);
    }

    @Override
    public Page<PostBean> page(RequestPost requestPost) {
        return null;
    }

    @Override
    public Long getPostComments(Long postId) {
        PostBean postBean = this.getOne(new LambdaQueryWrapper<PostBean>()
                .select(PostBean::getComments)
                .eq(PostBean::getId, postId));
        return postBean != null ? postBean.getComments() : null;
    }

    @Override
    public Long getPostViews(Long postId) {
        PostBean postBean = this.getOne(new LambdaQueryWrapper<PostBean>()
                .select(PostBean::getViews)
                .eq(PostBean::getId, postId));
        return postBean != null ? postBean.getViews() : null;
    }

    @Override
    public boolean updateComments(long id, long oldComments, long newComments) {
        return this.update(new LambdaUpdateWrapper<PostBean>()
                .set(PostBean::getComments, newComments)
                .eq(PostBean::getId, id)
                .eq(PostBean::getComments, oldComments));
    }

    @Override
    public boolean updateViews(long id, long oldViews, long newViews) {
        return this.update(new LambdaUpdateWrapper<PostBean>()
                .set(PostBean::getViews, newViews)
                .eq(PostBean::getId, id)
                .eq(PostBean::getViews, oldViews));
    }
}
