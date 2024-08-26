package com.doro.orm.api;

import com.doro.orm.bean.UserPostLikeBean;

import java.util.Map;

/**
 * 用户的赞与踩
 *
 * @author jiage
 */
public interface UserPostLikeService {

    boolean like(UserPostLikeBean userPostLikeBean);

    Map<Boolean, UserPostLikeBean> getPostLikes(Long postId);
}
