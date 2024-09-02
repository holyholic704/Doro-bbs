package com.doro.api.orm;

import com.doro.bean.PostBean;
import com.doro.api.model.request.RequestPost;
import com.doro.common.model.Page;

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
