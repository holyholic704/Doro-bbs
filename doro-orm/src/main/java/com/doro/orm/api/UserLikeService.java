package com.doro.orm.api;

import com.doro.orm.bean.UserLikeBean;

/**
 * 用户的赞与踩
 *
 * @author jiage
 */
public interface UserLikeService {

    boolean like(UserLikeBean userLikeBean);
}
