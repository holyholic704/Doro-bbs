package com.doro.core.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.orm.bean.UserBean;
import com.doro.common.constant.LoginConstant;
import com.doro.orm.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * 用户
 *
 * @author jiage
 */
@Service
public class UserService extends ServiceImpl<UserMapper, UserBean> {

    /**
     * 根据用户名获取用户信息
     */
    public UserBean getUserByUsername(String username) {
        return this.getOne(new LambdaQueryWrapper<UserBean>()
                .eq(UserBean::getUsername, username));
    }

    /**
     * 根据手机号获取用户信息
     */
    public UserBean getUserByPhone(String phone) {
        return this.getOne(new LambdaQueryWrapper<UserBean>()
                .eq(UserBean::getPhone, phone));
    }

    /**
     * 根据邮箱获取用户信息
     */
    public UserBean getUserByEmail(String email) {
        return this.getOne(new LambdaQueryWrapper<UserBean>()
                .eq(UserBean::getEmail, email));
    }

    /**
     * 该用户是否已存在
     *
     * @param username  用户名，可以为手机号、邮箱
     * @param loginType 登录方式，这里为查询方式
     */
    public boolean existUser(String username, String loginType) {
        // TODO 注意这里不要改为 swicth，后续 LoginConstant 可能会改为变量
        if (LoginConstant.USE_PHONE.equals(loginType)) {
            return this.hasPhoneUser(username);
        } else if (LoginConstant.USE_EMAIL.equals(loginType)) {
            return this.hasEmailUser(username);
        } else {
            return this.hasUser(username);
        }
    }

    /**
     * 是否有使用该用户名的用户
     */
    public boolean hasUser(String username) {
        return this.count(new LambdaQueryWrapper<UserBean>()
                .eq(UserBean::getUsername, username)) > 0;
    }

    /**
     * 是否有使用该手机的用户
     */
    public boolean hasEmailUser(String email) {
        return this.count(new LambdaQueryWrapper<UserBean>()
                .eq(UserBean::getEmail, email)) > 0;
    }

    /**
     * 是否有使用该手机的用户
     */
    public boolean hasPhoneUser(String phone) {
        return this.count(new LambdaQueryWrapper<UserBean>()
                .eq(UserBean::getUsername, phone)) > 0;
    }

    /**
     * 保存一个用户
     */
    public boolean saveUser(UserBean userBean) {
        return this.save(userBean);
    }
}
