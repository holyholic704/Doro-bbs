package com.doro.core.controller;

import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.LoginService;
import com.doro.res.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录注册
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("login")
    public ResponseResult<?> login(@RequestBody RequestUser requestUser) {
        return loginService.login(requestUser);
    }

    @PostMapping("register")
    public ResponseResult<?> register(@RequestBody RequestUser requestUser) {
        return loginService.register(requestUser);
    }
}
