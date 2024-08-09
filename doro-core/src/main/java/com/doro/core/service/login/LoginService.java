package com.doro.core.service.login;

import cn.hutool.core.util.StrUtil;
import com.doro.bean.User;
import com.doro.common.constant.Settings;
import com.doro.common.enumeration.LoginTypeEnum;
import com.doro.core.model.request.RequestUser;
import com.doro.core.model.response.ResponseUser;
import com.doro.core.service.UserService;
import com.doro.core.service.login.provider.MyAuthenticationToken;
import com.doro.core.utils.JwtUtil;
import com.doro.res.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 登录
     *
     * @param requestUser 请求信息
     * @return 是否成功登录
     */
    public ResponseResult<?> login(RequestUser requestUser) {
        // 获取登录方式
        LoginTypeEnum loginType = this.getLoginType(requestUser.getLoginType());
        // 参数校验，包括验证码校验
        validParam(requestUser, loginType);
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
                ResponseUser responseUser = this.login(requestUser, LoginTypeEnum.USE_PASSWORD);
                if (responseUser != null) {
                    return ResponseResult.success(responseUser);
                }
            }
        }
        return ResponseResult.error("注册失败");
    }

    private ResponseUser login(RequestUser requestUser, LoginTypeEnum loginType) {
        AbstractAuthenticationToken authenticationToken = this.getAuthenticationToken(requestUser, loginType);

        // 认证，失败抛出异常
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 将认证信息存储在 SecurityContextHolder 中
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return initToken(authentication);
    }

    /**
     * 获取登录方式
     */
    private LoginTypeEnum getLoginType(String loginType) {
        checkSupportLoginType(Settings.LOGIN_WITH_PHONE, LoginTypeEnum.USE_PHONE, loginType);
        checkSupportLoginType(Settings.LOGIN_WITH_EMAIL, LoginTypeEnum.USE_EMAIL, loginType);
        // 默认使用密码登录
        return LoginTypeEnum.USE_PASSWORD;
    }

    private LoginTypeEnum checkSupportLoginType(boolean supportLoginType, LoginTypeEnum loginTypeEnum, String loginType) {
        if (supportLoginType) {
            if (loginTypeEnum.getType().equals(loginType)) {
                return loginTypeEnum;
            }
        }
        return loginTypeEnum;
    }

    /**
     * 参数校验
     *
     * @param requestUser 请求信息
     * @param loginType   登录方式
     * @throws AuthenticationException 未通过验证抛出 AuthenticationException 异常
     */
    private void validParam(RequestUser requestUser, LoginTypeEnum loginType) throws AuthenticationException {
        if (StrUtil.isNotEmpty(requestUser.getUsername())) {
        }
    }

    /**
     * 根据登录方式获取不同的 AuthenticationToken
     *
     * @param requestUser 请求信息
     * @param loginType   登录方式
     * @return 相应的 AuthenticationToken
     */
    private AbstractAuthenticationToken getAuthenticationToken(RequestUser requestUser, LoginTypeEnum loginType) {
        switch (loginType) {
            case USE_PHONE:
            case USE_EMAIL:
                return new MyAuthenticationToken(requestUser.getUsername());
        }
        return new MyAuthenticationToken(requestUser.getUsername(), requestUser.getPassword());
    }

    /**
     * 返回含有 Token 的响应信息
     *
     * @param authentication 认证信息
     * @return 响应信息
     */
    public ResponseUser initToken(Authentication authentication) {
//        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
//        // 生成token
//        String token = jwtUtil.generate(principal);
//        // 缓存token
//        redisTemplate.opsForValue().set(username, token, expired, TimeUnit.SECONDS);
        return null;
    }
}
