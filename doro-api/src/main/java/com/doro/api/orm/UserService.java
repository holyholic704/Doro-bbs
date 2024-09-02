package com.doro.api.orm;

import com.doro.bean.UserBean;

/**
 * 用户
 *
 * @author jiage
 */
public interface UserService {

    /**
     * 根据用户名获取用户信息
     */
    UserBean getUserByUsername(String username);

    /**
     * 根据手机号获取用户信息
     */
    UserBean getUserByPhone(String phone);

    /**
     * 根据邮箱获取用户信息
     */
    UserBean getUserByEmail(String email);

    /**
     * 是否有使用该用户名的用户
     */
    boolean hasUser(String username);

    /**
     * 是否有使用该手机的用户
     */
    boolean hasEmailUser(String email);

    /**
     * 是否有使用该手机的用户
     */
    boolean hasPhoneUser(String phone);

    /**
     * 保存一个用户
     */
    boolean saveUser(UserBean userBean);
}
