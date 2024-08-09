package com.doro.core.service.login.valid;

import com.doro.common.constant.LoginConstant;
import com.doro.common.constant.Settings;
import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;

public class PasswordValidAndInitChainHandler extends AbstractValidAndInitChainHandler {

    @Override
    protected MyAuthenticationToken handle(RequestUser requestUser) {
        this.validPassword(requestUser.getPassword());

        String loginType = LoginConstant.USE_PASSWORD;

        if (this.validPhone(requestUser.getUsername())) {
            loginType = LoginConstant.USE_PHONE;
            isSupportLoginType(Settings.LOGIN_PASSWORD_WITH_PHONE);
        }

        if (this.validEmail(requestUser.getUsername())) {
            loginType = LoginConstant.USE_EMAIL;
            isSupportLoginType(Settings.LOGIN_PASSWORD_WITH_EMAIL);
        }
        return initAuthenticationToken(requestUser, loginType, true);
    }
}
