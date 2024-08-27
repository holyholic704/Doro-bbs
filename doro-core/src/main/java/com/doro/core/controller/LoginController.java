package com.doro.core.controller;

import com.doro.common.response.ResponseResult;
import com.doro.core.model.response.ResponseUser;
import com.doro.core.service.login.LoginService;
import com.doro.orm.model.request.RequestUser;
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
        ResponseUser responseUser = loginService.login(requestUser);
        return responseUser != null ? ResponseResult.success(responseUser) : ResponseResult.error("登录失败");
    }

    @PostMapping("register")
    public ResponseResult<?> register(@RequestBody RequestUser requestUser) {
        return loginService.register(requestUser);
    }
}
