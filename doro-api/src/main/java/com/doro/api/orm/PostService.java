package com.doro.api.orm;

import com.doro.bean.PostBean;
import com.doro.api.model.request.RequestPost;
import com.doro.common.model.Page;

import java.util.List;

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

    boolean updatePost(PostBean postBean);

    /**
     * 分页查询
     */
    List<PostBean> page(RequestPost requestPost);

    Long getPostViews(Long postId);

    Long getPostComments(Long postId);

    boolean updateComments(long id);

    boolean updateViews(long id, long views);
}
