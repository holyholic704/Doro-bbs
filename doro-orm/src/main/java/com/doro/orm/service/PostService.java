package com.doro.orm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.orm.bean.PostBean;
import com.doro.orm.mapper.PostMapper;
import com.doro.orm.request.RequestPost;
import org.springframework.stereotype.Service;

/**
 * 帖子
 *
 * @author jiage
 */
@Service
public class PostService extends ServiceImpl<PostMapper, PostBean> {

    public boolean savePost(PostBean postBean) {
        return this.save(postBean);
    }

    public Page<PostBean> page(RequestPost requestPost) {
        return this.page(requestPost.asPage());
    }
}
