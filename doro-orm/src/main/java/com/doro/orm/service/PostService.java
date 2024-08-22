package com.doro.orm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.orm.bean.Post;
import com.doro.orm.mapper.PostMapper;
import org.springframework.stereotype.Service;

/**
 * @author jiage
 */
@Service
public class PostService extends ServiceImpl<PostMapper, Post> {

    public boolean savePost(Post post) {
        return this.save(post);
    }

    public Page<Post> page(Post post, int current, int size) {
        return this.page(new Page<>(current, size));
    }
}
