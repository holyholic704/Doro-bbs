package com.doro.api.orm;

import com.doro.bean.UserLikeBean;

/**
 * 用户的赞与踩
 *
 * @author jiage
 */
public interface UserLikeService {

    boolean like(UserLikeBean userLikeBean);
}
