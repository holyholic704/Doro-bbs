package com.doro.core.service.login.valid;

import cn.hutool.core.util.ReUtil;
import com.doro.common.constant.LoginConstant;
import com.doro.common.constant.RegexConstant;
import com.doro.common.constant.Settings;
import com.doro.core.exception.MyAuthenticationException;
import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;

/**
 * 密码校验
 */
public class PasswordValidAndInitChainHandler extends AbstractValidAndInitChainHandler {

    @Override
    protected MyAuthenticationToken handle(RequestUser requestUser) {
        this.validPassword(requestUser.getPassword());

        String loginType = LoginConstant.USE_PASSWORD;

        if (ReUtil.isMatch(RegexConstant.PHONE, requestUser.getUsername())) {
            loginType = LoginConstant.USE_PHONE;
            isSupportLoginType(Settings.LOGIN_PASSWORD_WITH_PHONE);
        }

        if (ReUtil.isMatch(RegexConstant.EMAIL, requestUser.getUsername())) {
            loginType = LoginConstant.USE_EMAIL;
            isSupportLoginType(Settings.LOGIN_PASSWORD_WITH_EMAIL);
        }
        return initAuthenticationToken(requestUser, loginType, true);
    }

    /**
     * 密码校验
     *
     * @param password 密码
     */
    private void validPassword(String password) {
        if (!ReUtil.isMatch(RegexConstant.PASSWORD, password)) {
            throw new MyAuthenticationException("密码格式不正确");
        }
    }
}
