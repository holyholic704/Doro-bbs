package com.doro.core.service.login;

import com.doro.core.model.request.RequestUser;
import com.doro.core.model.response.ResponseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 登录
     *
     * @param requestUser 请求信息
     * @return 是否成功登录
     */
    public ResponseUser login(RequestUser requestUser) {
        AbstractAuthenticationToken authenticationToken;
        if (valid(requestUser) && (authenticationToken = getAuthenticationToken(requestUser)) != null) {
            // 认证
            Authentication authentication;
            try {
                authentication = authenticationManager.authenticate(authenticationToken);
            } catch (AuthenticationException e) {
                // 认证失败
                return null;
            }

            // 将认证信息存储在 SecurityContextHolder 中
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return initToken(authentication, requestUser);
        }
        return null;
    }


    /**
     * 参数校验，如果需要
     *
     * @param requestUser 请求信息
     * @return 是否通过校验
     */
    public boolean valid(RequestUser requestUser) {
        return true;
    }

    /**
     * 根据登录方式获取不同的 AuthenticationToken
     *
     * @param requestUser 请求信息
     * @return 相应的 AuthenticationToken
     */
    abstract AbstractAuthenticationToken getAuthenticationToken(RequestUser requestUser);

    abstract ResponseUser initToken(Authentication authentication, RequestUser requestUser);
}
