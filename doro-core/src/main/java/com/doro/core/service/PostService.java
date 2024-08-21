package com.doro.core.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.bean.Post;
import com.doro.core.model.request.RequestPost;
import com.doro.orm.mapper.PostMapper;

/**
 * 文章服务
 *
 * @author jiage
 */
public class PostService extends ServiceImpl<PostMapper, Post> {

    public boolean savePost(RequestPost requestPost) {
        // TODO 获取用户信息
        // TODO 发帖权限
        return this.save(requestPost);
    }

    public boolean savePost(Post post) {
        return this.save(post);
    }
}
