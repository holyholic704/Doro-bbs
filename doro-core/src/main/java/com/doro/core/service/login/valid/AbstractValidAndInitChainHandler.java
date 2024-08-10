package com.doro.core.service.login.valid;

import com.doro.core.exception.MyAuthenticationException;
import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;

/**
 * 登录参数验证
 */
public abstract class AbstractValidAndInitChainHandler {

    private AbstractValidAndInitChainHandler nextHandler;

    public void setNext(AbstractValidAndInitChainHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    protected abstract MyAuthenticationToken handle(RequestUser requestUser);

    protected MyAuthenticationToken initAuthenticationToken(RequestUser requestUser, String loginType, boolean usePassword) {
        MyAuthenticationToken authenticationToken = new MyAuthenticationToken(requestUser.getUsername(), requestUser.getPassword());
        authenticationToken.setLoginType(loginType);
        authenticationToken.setUsePassword(usePassword);
        return authenticationToken;
    }

    protected void isSupportLoginType(boolean isSupport) {
        if (!isSupport) {
            throw new MyAuthenticationException("不支持的登录方式");
        }
    }

    protected MyAuthenticationToken process(RequestUser requestUser) {
        MyAuthenticationToken authenticationToken = handle(requestUser);
        if (authenticationToken != null) {
            return authenticationToken;
        }
        if (this.nextHandler != null) {
            return this.nextHandler.process(requestUser);
        }
        return null;
    }
}
