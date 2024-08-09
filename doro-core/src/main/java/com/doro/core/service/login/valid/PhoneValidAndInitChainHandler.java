package com.doro.core.service.login.valid;

import com.doro.common.constant.LoginConstant;
import com.doro.common.constant.Settings;
import com.doro.core.exception.MyAuthenticationException;
import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;

public class PhoneValidAndInitChainHandler extends AbstractValidAndInitChainHandler {

    @Override
    protected MyAuthenticationToken handle(RequestUser requestUser) {
        if (LoginConstant.USE_PHONE.equals(requestUser.getLoginType())) {
            if (!Settings.LOGIN_SUPPORT_PHONE) {
                throw new MyAuthenticationException("不支持的登录方式");
            }

            if (!this.validPhone(requestUser.getUsername())) {
                throw new MyAuthenticationException("手机格式错误");
            }

            return initAuthenticationToken(requestUser, LoginConstant.USE_PHONE, false);
        }
        return doNextHandler(requestUser);
    }

}
