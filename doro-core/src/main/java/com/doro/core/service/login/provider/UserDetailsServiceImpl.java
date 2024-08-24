package com.doro.core.service.login.provider;

import com.doro.common.constant.LoginConstant;
import com.doro.common.exception.ValidException;
import com.doro.orm.api.UserService;
import com.doro.orm.bean.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 自定义用户信息加载
 *
 * @author jiage
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserByUsername(String username, String loginType) throws UsernameNotFoundException {
        UserBean userBean = this.getUserByType(username, loginType);
        userNotExist(userBean != null);
        return this.buildFromUser(userBean);
    }

    /**
     * 使用密码登录时，是否支持使用用户名、手机号、邮箱登录
     * 根据输入的不同，使用不同的获取用户的方法
     *
     * @param username  用户名、手机号、邮箱
     * @param loginType 登录方式
     * @return 用户信息
     */
    private UserBean getUserByType(String username, String loginType) {
        if (LoginConstant.USE_PHONE.equals(loginType)) {
            return userService.getUserByPhone(username);
        } else if (LoginConstant.USE_EMAIL.equals(loginType)) {
            return userService.getUserByEmail(username);
        } else {
            return userService.getUserByUsername(username);
        }
    }

    private void userNotExist(boolean exist) {
        if (!exist) {
            throw new ValidException("用户不存在");
        }
    }

    private UserDetails buildFromUser(UserBean userBean) {
        return new MyUserDetails()
                .setId(userBean.getId())
                .setUsername(userBean.getUsername())
                .setPassword(userBean.getPassword());
    }
}
