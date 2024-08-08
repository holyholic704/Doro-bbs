package com.doro.core.service.login;

import cn.hutool.core.util.StrUtil;
import com.doro.bean.User;
import com.doro.common.constant.LoginConstant;
import com.doro.core.model.request.RequestUser;
import com.doro.core.model.response.ResponseUser;
import com.doro.core.service.UserService;
import com.doro.res.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 登录注册服务
 */
@Service
public class LoginService {

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
        // 默认使用密码登录
        String loginType = LoginConstant.USE_PASSWORD;
        ResponseUser responseUser = this.login(requestUser, loginType);
        return responseUser != null ? ResponseResult.success(responseUser) : ResponseResult.error("登录失败");
    }

    /**
     * 注册
     *
     * @param requestUser 请求信息
     * @return 是否注册成功
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<?> register(RequestUser requestUser) {
        // TODO 是否需要发送验证码
        if (StrUtil.isNotEmpty(requestUser.getUsername()) && StrUtil.isNotEmpty(requestUser.getPassword()) && userService.notExist(requestUser.getUsername())) {
            User register = new User()
                    .setUsername(requestUser.getUsername())
                    .setPassword(bCryptPasswordEncoder.encode(requestUser.getPassword()));

            if (userService.saveUser(register)) {
                // 调用登录方法
                ResponseUser responseUser = this.login(requestUser, LoginConstant.USE_PASSWORD);
                if (responseUser != null) {
                    return ResponseResult.success(responseUser);
                }
            }
        }
        return ResponseResult.error("注册失败");
    }

    public ResponseUser login(RequestUser requestUser, String loginType) {
//        AbstractAuthenticationToken authenticationToken;
//        // 参数校验，一般为验证码的校验
//        valid(requestUser);
//        if ((authenticationToken = getAuthenticationToken(requestUser)) != null) {
//            // 认证，失败抛出异常
//            Authentication authentication = authenticationManager.authenticate(authenticationToken);
//
//            // 将认证信息存储在 SecurityContextHolder 中
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            return initToken(authentication);
//        }
        return null;
    }
//
//    /**
//     * 参数校验，如果需要
//     *
//     * @param requestUser 请求信息
//     * @throws AuthenticationException 未通过验证抛出 AuthenticationException 异常
//     */
//    public void valid(RequestUser requestUser) throws AuthenticationException {
//    }
}
