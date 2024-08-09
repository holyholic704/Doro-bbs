package com.doro.core.service.login.valid;

import cn.hutool.core.lang.RegexPool;
import cn.hutool.core.util.ReUtil;
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

    protected MyAuthenticationToken doNextHandler(RequestUser requestUser) {
        if (this.nextHandler != null) {
            return this.nextHandler.handle(requestUser);
        }
        return null;
    }

    protected abstract MyAuthenticationToken handle(RequestUser requestUser);

    protected boolean validPhone(String phone) {
        return ReUtil.isMatch(RegexPool.MOBILE, phone);
    }

    protected boolean validEmail(String email) {
        return ReUtil.isMatch(RegexPool.EMAIL, email);
    }

    protected MyAuthenticationToken initAuthenticationToken(RequestUser requestUser, String loginType, boolean usePassword) {
        MyAuthenticationToken authenticationToken = new MyAuthenticationToken(requestUser.getUsername(), requestUser.getPassword());
        authenticationToken.setLoginType(loginType);
        authenticationToken.setUsePassword(usePassword);
        return authenticationToken;
    }

    public MyAuthenticationToken process(RequestUser requestUser) {
        MyAuthenticationToken authenticationToken = handle(requestUser);
        return doNextHandler(requestUser);
    }

    protected void isSupportLoginType(boolean isSupport) {
        if (!isSupport) {
            throw new MyAuthenticationException("不支持的登录方式");
        }
    }
}
