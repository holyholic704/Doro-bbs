package com.doro.api.orm;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doro.api.bean.PostBean;
import com.doro.api.model.request.RequestPost;

/**
 * 帖子
 *
 * @author jiage
 */
public interface PostService {

    /**
     * 根据 ID 获取一个帖子
     */
    PostBean getPostById(Long postId);

    /**
     * 保存一个帖子
     */
    boolean savePost(PostBean postBean);

    /**
     * 分页查询
     */
    Page<PostBean> page(RequestPost requestPost);

    Long getPostViews(Long postId);

    long getPostComments(Long postId);
}
