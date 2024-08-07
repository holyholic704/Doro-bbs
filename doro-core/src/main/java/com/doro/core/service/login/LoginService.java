package com.doro.core.service.login;

import cn.hutool.core.util.StrUtil;
import com.doro.bean.user.User;
import com.doro.common.constant.LoginConstant;
import com.doro.common.model.res.ResponseResult;
import com.doro.core.model.request.RequestUser;
import com.doro.core.model.response.ResponseUser;
import com.doro.core.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 登录注册服务
 */
@Service
public class LoginService {

    @Autowired
    private Map<String, BaseLoginService> loginServiceMap;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserService userService;

    /**
     * 登录
     *
     * @param requestUser 请求信息
     * @return 是否成功登录
     */
    public ResponseResult<?> login(RequestUser requestUser) {
        BaseLoginService baseLoginService;
        // 默认使用密码登录
        if (StrUtil.isEmpty(requestUser.getLoginType()) || (baseLoginService = loginServiceMap.get(requestUser.getLoginType())) == null) {
            baseLoginService = loginServiceMap.get(LoginConstant.USE_PASSWORD);
        }
        ResponseUser responseUser = baseLoginService.login(requestUser);
        return responseUser != null ? ResponseResult.success(responseUser) : ResponseResult.error("登录失败");
    }

    /**
     * 注册
     *
     * @param requestUser 请求信息
     * @return 是否注册成功
     */
    public ResponseResult<?> register(RequestUser requestUser) {
        // TODO 是否需要发送验证码
        if (StrUtil.isNotEmpty(requestUser.getUsername()) && StrUtil.isNotEmpty(requestUser.getPassword()) && userService.notExist(requestUser.getUsername())) {
            User register = new User()
                    .setUsername(requestUser.getUsername())
                    .setPassword(bCryptPasswordEncoder.encode(requestUser.getPassword()));
            userService.save(register);

            // 调用登录方法
            ResponseUser responseUser = loginServiceMap.get(LoginConstant.USE_PASSWORD).login(requestUser);
            return responseUser != null ? ResponseResult.success(responseUser) : ResponseResult.error("注册失败");
        }
        return ResponseResult.error("注册失败");
    }
}
