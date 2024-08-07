package com.doro.core.service.login.details;

import com.doro.api.bean.user.User;
import com.doro.core.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UseCodeUserDetails implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userService.getByPhone(phone);
        Assert.isTrue(user != null, "当前用户不存在");
        return user;
    }
}