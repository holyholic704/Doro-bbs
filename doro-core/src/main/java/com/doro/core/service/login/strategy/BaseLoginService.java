package com.doro.core.service.login.strategy;

import com.doro.core.model.request.RequestUser;
import com.doro.core.model.response.ResponseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 基础登录方法
 * <p>
 * 所有的登录方式都要继承本类
 */
public abstract class BaseLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 登录
     *
     * @param requestUser 请求信息
     * @return 是否成功登录
     */
    public ResponseUser login(RequestUser requestUser) {
        AbstractAuthenticationToken authenticationToken;
        // 参数校验，一般为验证码的校验
        valid(requestUser);
        if ((authenticationToken = getAuthenticationToken(requestUser)) != null) {
            // 认证，失败抛出异常
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 将认证信息存储在 SecurityContextHolder 中
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return initToken(authentication);
        }
        return null;
    }


    /**
     * 参数校验，如果需要
     *
     * @param requestUser 请求信息
     * @throws AuthenticationException 未通过验证抛出 AuthenticationException 异常
     */
    public void valid(RequestUser requestUser) throws AuthenticationException {
    }

    /**
     * 根据登录方式获取不同的 AuthenticationToken
     *
     * @param requestUser 请求信息
     * @return 相应的 AuthenticationToken
     */
    abstract AbstractAuthenticationToken getAuthenticationToken(RequestUser requestUser);

    /**
     * 返回含有 Token 的响应信息
     *
     * @param authentication 认证信息
     * @return 响应信息
     */
    public ResponseUser initToken(Authentication authentication) {
//        LoadUser principal = (LoadUser) authentication.getPrincipal();
//        // 生成token
//        String token = jwtUtil.generate(principal);
//        // 缓存token
//        redisTemplate.opsForValue().set(username, token, expired, TimeUnit.SECONDS);
        return null;
    }
}
