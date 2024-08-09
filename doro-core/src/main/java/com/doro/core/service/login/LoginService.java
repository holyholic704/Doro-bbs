package com.doro.core.service.login;

import cn.hutool.core.lang.RegexPool;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.doro.bean.User;
import com.doro.common.constant.LoginConstant;
import com.doro.common.constant.Settings;
import com.doro.common.enumeration.LoginTypeEnum;
import com.doro.core.exception.MyAuthenticationException;
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
        MyAuthenticationToken authenticationToken = new MyAuthenticationToken(requestUser.getUsername(), requestUser.getPassword());
//        authenticationToken.setLoginType();
//        authenticationToken.setUsePassword();

        // 认证，失败抛出异常
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 将认证信息存储在 SecurityContextHolder 中
        SecurityContextHolder.getContext().setAuthentication(authentication);

//        return initToken(authentication);
//        return responseUser != null ? ResponseResult.success(responseUser) : ResponseResult.error("登录失败");
        return null;
    }

    /**
     * 注册
     *
     * @param requestUser 请求信息
     * @return 是否注册成功
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<?> register(RequestUser requestUser) {
        if (StrUtil.isNotEmpty(requestUser.getUsername()) && StrUtil.isNotEmpty(requestUser.getPassword()) && userService.notExist(requestUser.getUsername())) {
            User register = new User()
                    .setUsername(requestUser.getUsername())
                    .setPassword(bCryptPasswordEncoder.encode(requestUser.getPassword()));

            if (userService.saveUser(register)) {
                // 调用登录方法
                ResponseUser responseUser = this.authenticate(requestUser);
                if (responseUser != null) {
                    return ResponseResult.success(responseUser);
                }
            }
        }
        return ResponseResult.error("注册失败");
    }

    private ResponseUser authenticate(RequestUser requestUser) {
        AbstractAuthenticationToken authenticationToken = new MyAuthenticationToken(requestUser.getUsername(), requestUser.getPassword());

        // 认证，失败抛出异常
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 将认证信息存储在 SecurityContextHolder 中
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return initToken(authentication);
    }

    /**
     * 参数校验
     *
     * @param requestUser 请求信息
     * @param loginType   登录方式
     * @throws AuthenticationException 未通过验证抛出 AuthenticationException 异常
     */

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
//                return new MyAuthenticationToken(requestUser.getUsername());
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

    private void t(RequestUser requestUser) {
        String username = requestUser.getUsername();
        if (StrUtil.isNotEmpty(username)) {
            if (LoginConstant.USE_PHONE.equals(requestUser.getLoginType())) {
                this.isSupportLoginType(Settings.LOGIN_SUPPORT_PHONE);

                if (!this.validPhone(requestUser.getUsername())) {
                    throw new MyAuthenticationException("手机格式错误");
                }

//                return initAuthenticationToken(requestUser, LoginConstant.USE_PHONE, false);
            }

            if (LoginConstant.USE_EMAIL.equals(requestUser.getLoginType())) {
                this.isSupportLoginType(Settings.LOGIN_SUPPORT_EMAIL);

                if (!this.validEmail(requestUser.getUsername())) {
                    throw new MyAuthenticationException("邮箱格式错误");
                }

//                return initAuthenticationToken(requestUser, LoginConstant.USE_EMAIL, false);
            }

            if (StrUtil.isEmpty(requestUser.getPassword())) {
                throw new MyAuthenticationException("密码为空");
            }

            String loginType = LoginConstant.USE_PASSWORD;

            if (this.validPhone(requestUser.getUsername())) {
                loginType = LoginConstant.USE_PHONE;
                isSupportLoginType(Settings.LOGIN_PASSWORD_WITH_PHONE);
            }

            if (this.validEmail(requestUser.getUsername())) {
                loginType = LoginConstant.USE_EMAIL;
                isSupportLoginType(Settings.LOGIN_PASSWORD_WITH_EMAIL);
            }
        }
    }

    private void isSupportLoginType(boolean isSupport) {
        if (!isSupport) {
            throw new MyAuthenticationException("不支持的登录方式");
        }
    }

    protected void validPhone(String phone) {
        if (!ReUtil.isMatch(RegexPool.MOBILE, phone)) {
            throw new MyAuthenticationException("手机号格式错误");
        }
    }

    protected boolean validEmail(String email) {
        if (!ReUtil.isMatch(RegexPool.EMAIL, email)) {
            throw new MyAuthenticationException("邮箱格式错误");
        }
    }
}
