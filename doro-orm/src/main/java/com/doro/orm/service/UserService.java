package com.doro.orm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.orm.bean.User;
import com.doro.common.constant.LoginConstant;
import com.doro.orm.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * 用户服务
 *
 * @author jiage
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    /**
     * 根据用户名获取用户信息
     */
    public User getUserByUsername(String username) {
        return this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
    }

    /**
     * 根据手机号获取用户信息
     */
    public User getUserByPhone(String phone) {
        return this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, phone));
    }

    /**
     * 根据邮箱获取用户信息
     */
    public User getUserByEmail(String email) {
        return this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email));
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
        return this.count(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)) > 0;
    }

    /**
     * 是否有使用该手机的用户
     */
    public boolean hasEmailUser(String email) {
        return this.count(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email)) > 0;
    }

    /**
     * 是否有使用该手机的用户
     */
    public boolean hasPhoneUser(String phone) {
        return this.count(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, phone)) > 0;
    }

    /**
     * 保存一个用户
     */
    public boolean saveUser(User user) {
        return this.save(user);
    }
}
