package com.doro.core.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 自定义权限异常
 */
public class MyAuthenticationException extends AuthenticationException {

    public MyAuthenticationException(String msg) {
        super(msg);
    }

}
