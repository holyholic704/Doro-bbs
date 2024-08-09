package com.doro.core.service.login.provider;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

public class MyAuthenticationToken extends AbstractAuthenticationToken {

    /**
     * 用户名
     */
    private final Object principal;

    /**
     * 密码
     */
    private final Object credentials;

    /**
     * 登录方式
     */
    private String loginType;

    /**
     * 是否使用密码
     */
    private boolean usePassword = true;

    public MyAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public MyAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated, "必须使用构造器创建");
        super.setAuthenticated(false);
    }

    public String getLoginType() {
        return loginType;
    }

    public boolean isUsePassword() {
        return usePassword;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public void setUsePassword(boolean usePassword) {
        this.usePassword = usePassword;
    }
}
