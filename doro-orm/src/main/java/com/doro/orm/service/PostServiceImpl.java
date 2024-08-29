package com.doro.orm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.orm.api.PostService;
import com.doro.orm.bean.PostBean;
import com.doro.orm.mapper.PostMapper;
import com.doro.orm.model.request.RequestPost;
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
        return this.page(requestPost.asPage());
    }

    @Override
    public Long getPostViews(Long postId) {
        PostBean postBean = this.getOne(new LambdaQueryWrapper<PostBean>()
                .select(PostBean::getViews)
                .eq(PostBean::getId, postId));
        return postBean != null ? postBean.getViews() : null;
    }

}
