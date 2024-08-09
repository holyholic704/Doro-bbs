package com.doro.core.service.login.valid;

import com.doro.common.constant.LoginConstant;
import com.doro.common.constant.Settings;
import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;

public class EmailValidAndInitChainHandler extends AbstractValidAndInitChainHandler {
    @Override
    protected MyAuthenticationToken handle(RequestUser requestUser) {
        if (LoginConstant.USE_EMAIL.equals(requestUser.getLoginType())) {
            isSupportLoginType(Settings.LOGIN_SUPPORT_EMAIL);
            validEmail(requestUser.getUsername());

            return initAuthenticationToken(requestUser, LoginConstant.USE_EMAIL, false);
        }
        return doNextHandler(requestUser);
    }
}
