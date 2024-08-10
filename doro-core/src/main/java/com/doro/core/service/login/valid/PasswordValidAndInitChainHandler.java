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

    /**
     * 校验逻辑
     */
    @Override
    protected MyAuthenticationToken handle(RequestUser requestUser) {
        this.validPassword(requestUser.getPassword());

        // 默认使用用户名密码登录
        String loginType = LoginConstant.USE_PASSWORD;

        // 判断传入的是否是手机号，如果是再判断是否支持该方式使用密码登录
        if (ReUtil.isMatch(RegexConstant.PHONE, requestUser.getUsername())) {
            isSupportLoginType(Settings.LOGIN_PASSWORD_WITH_PHONE);
            loginType = LoginConstant.USE_PHONE;
        }

        // 判断传入的是否是邮箱，如果是再判断是否支持该方式使用密码登录
        if (ReUtil.isMatch(RegexConstant.EMAIL, requestUser.getUsername())) {
            isSupportLoginType(Settings.LOGIN_PASSWORD_WITH_EMAIL);
            loginType = LoginConstant.USE_EMAIL;
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
