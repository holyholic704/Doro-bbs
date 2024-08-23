package com.doro.core.controller;

import com.doro.orm.request.RequestUser;
import com.doro.common.response.ResponseResult;
import com.doro.core.service.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录注册
 *
 * @author jiage
 */
@RestController
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("login")
    public ResponseResult<?> login(@RequestBody RequestUser requestUser) {
        return loginService.login(requestUser);
    }

    @PostMapping("register")
    public ResponseResult<?> register(@RequestBody RequestUser requestUser) {
        return loginService.register(requestUser);
    }
}
