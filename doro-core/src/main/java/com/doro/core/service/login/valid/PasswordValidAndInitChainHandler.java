package com.doro.core.service.login.valid;

import cn.hutool.core.util.StrUtil;
import com.doro.common.constant.LoginConstant;
import com.doro.common.constant.Settings;
import com.doro.core.exception.MyAuthenticationException;
import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;

public class PasswordValidAndInitChainHandler extends AbstractValidAndInitChainHandler {

    @Override
    protected MyAuthenticationToken handle(RequestUser requestUser) {
        if (StrUtil.isEmpty(requestUser.getPassword())) {
            throw new MyAuthenticationException("密码为空");
        }

        String loginType = LoginConstant.USE_PASSWORD;

        if (this.validPhone(requestUser.getUsername())) {
            loginType = LoginConstant.USE_PHONE;
            if (!Settings.LOGIN_PASSWORD_WITH_PHONE) {
                throw new MyAuthenticationException("不支持的登录方式");
            }
        }

        if (this.validEmail(requestUser.getUsername())) {
            loginType = LoginConstant.USE_EMAIL;
            if (!Settings.LOGIN_PASSWORD_WITH_EMAIL) {
                throw new MyAuthenticationException("不支持的登录方式");
            }
        }
        return initAuthenticationToken(requestUser, loginType, true);
    }
}
