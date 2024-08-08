package com.doro.core.service.login.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

public class UsePasswordAuthenticationToken extends AbstractAuthenticationToken {

    /**
     * 用户名
     */
    private final Object principal;

    /**
     * 密码
     */
    private final Object credentials;

    /**
     * 不使用密码校验
     */
    private boolean withoutPassword = false;

    public UsePasswordAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public UsePasswordAuthenticationToken(Object principal, Object credentials, boolean withoutPassword) {
        super(null);
        this.principal = principal;
        this.credentials = null;
        this.withoutPassword = true;
        setAuthenticated(false);
    }

    public UsePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
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

    public boolean withoutPassword() {
        return withoutPassword;
    }
}
