package com.doro.orm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.orm.api.UserService;
import com.doro.orm.bean.UserBean;
import com.doro.orm.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * 用户
 *
 * @author jiage
 */
@Service
class UserServiceImpl extends ServiceImpl<UserMapper, UserBean> implements UserService {

    @Override
    public UserBean getUserByUsername(String username) {
        return this.getOne(new LambdaQueryWrapper<UserBean>()
                .eq(UserBean::getUsername, username));
    }

    @Override
    public UserBean getUserByPhone(String phone) {
        return this.getOne(new LambdaQueryWrapper<UserBean>()
                .eq(UserBean::getPhone, phone));
    }

    @Override
    public UserBean getUserByEmail(String email) {
        return this.getOne(new LambdaQueryWrapper<UserBean>()
                .eq(UserBean::getEmail, email));
    }

    @Override
    public boolean hasUser(String username) {
        return this.count(new LambdaQueryWrapper<UserBean>()
                .eq(UserBean::getUsername, username)) > 0;
    }

    @Override
    public boolean hasEmailUser(String email) {
        return this.count(new LambdaQueryWrapper<UserBean>()
                .eq(UserBean::getEmail, email)) > 0;
    }

    @Override
    public boolean hasPhoneUser(String phone) {
        return this.count(new LambdaQueryWrapper<UserBean>()
                .eq(UserBean::getUsername, phone)) > 0;
    }

    @Override
    public boolean saveUser(UserBean userBean) {
        return this.save(userBean);
    }
}
