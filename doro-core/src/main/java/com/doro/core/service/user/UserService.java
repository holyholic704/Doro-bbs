package com.doro.core.service.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.bean.user.User;
import com.doro.mapper.user.UserMapper;
import org.springframework.stereotype.Service;

/**
 * 用户服务
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    /**
     * 根据用户名获取用户信息
     */
    public User getByUsername(String username) {
        return this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
    }

    /**
     * 根据手机号获取用户信息
     */
    public User getByPhone(String phone) {
        return this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, phone));
    }

    /**
     * 根据邮箱获取用户信息
     */
    public User getByEmail(String email) {
        return this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email));
    }
}
