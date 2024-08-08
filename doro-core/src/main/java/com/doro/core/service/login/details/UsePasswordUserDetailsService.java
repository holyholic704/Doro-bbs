package com.doro.core.service.login.details;

import cn.hutool.core.lang.RegexPool;
import cn.hutool.core.util.ReUtil;
import com.doro.bean.user.User;
import com.doro.common.constant.Settings;
import com.doro.core.service.login.LoadUser;
import com.doro.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UsePasswordUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.getUserByType(username);
        Assert.isTrue(user != null, "当前用户不存在");
        return this.buildFromUser(user);
    }

    /**
     * 使用密码登录时，是否支持使用用户名、手机号、邮箱登录
     * 根据输入的不同，使用不同的获取用户的方法
     *
     * @param username 用户名、手机号、邮箱
     * @return 用户信息
     */
    private User getUserByType(String username) {
        // 使用密码登录时，允许输入手机号
        if (Settings.USE_PASSWORD_WITH_PHONE && ReUtil.contains(RegexPool.MOBILE, username)) {
            return userService.getByPhone(username);
        }
        // 使用密码登录时，允许使用邮箱
        if (Settings.USE_PASSWORD_WITH_EMAIL && ReUtil.contains(RegexPool.EMAIL, username)) {
            return userService.getByEmail(username);
        }
        return userService.getByUsername(username);
    }

    private UserDetails buildFromUser(User user) {
        return new LoadUser()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setPassword(user.getPassword());
    }
}
