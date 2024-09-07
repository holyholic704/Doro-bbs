package com.doro.orm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.api.model.request.RequestPost;
import com.doro.api.orm.PostService;
import com.doro.bean.PostBean;
import com.doro.orm.mapper.PostMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public boolean updatePost(PostBean postBean) {
        return this.update(new LambdaUpdateWrapper<PostBean>()
                .eq(PostBean::getId, postBean.getId())
                .set(postBean.getContent() != null, PostBean::getContent, postBean.getContent())
                .set(postBean.getTitle() != null, PostBean::getTitle, postBean.getTitle()));
    }

    @Override
    public List<PostBean> page(RequestPost requestPost) {
        int current = requestPost.getCurrent();
        int size = requestPost.getSize();
        int from = (current == 0 ? current : current - 1) * size;
        return this.getBaseMapper().page(requestPost, from, size);
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
    public boolean updateComments(long id) {
        return this.getBaseMapper().updateComments(id);
    }

    @Override
    public boolean updateViews(long id, long views) {
        return this.update(new LambdaUpdateWrapper<PostBean>()
                .set(PostBean::getViews, views)
                .eq(PostBean::getId, id)
                .lt(PostBean::getViews, views));
    }
}
