package com.doro.core.service.login.valid;

import cn.hutool.core.util.ReUtil;
import com.doro.common.constant.RegexConstant;
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

    protected void validPhone(String phone) {
        if (!ReUtil.isMatch(RegexConstant.PHONE, phone)) {
            throw new MyAuthenticationException("手机号码格式错误");
        }
    }

    protected void validEmail(String email) {
        if (!ReUtil.isMatch(RegexConstant.EMAIL, email)) {
            throw new MyAuthenticationException("邮箱格式错误");
        }
    }

    protected void validPassword(String password) {
        if (!ReUtil.isMatch(RegexConstant.PASSWORD, password)) {
            throw new MyAuthenticationException("密码格式不正确");
        }
    }
}
