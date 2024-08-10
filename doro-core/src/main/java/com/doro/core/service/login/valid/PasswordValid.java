package com.doro.core.service.login.valid;

import cn.hutool.core.util.ReUtil;
import com.doro.common.constant.LoginConstant;
import com.doro.common.constant.RegexConstant;
import com.doro.common.constant.Settings;
import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;
import com.doro.core.utils.LoginValidUtil;

/**
 * 使用密码登录校验
 */
public class PasswordValid implements LoginValid<RequestUser, MyAuthenticationToken> {

    @Override
    public MyAuthenticationToken test(RequestUser requestUser) {
        LoginValidUtil.validPassword(requestUser.getPassword());

        // 默认使用用户名密码登录
        String loginType = LoginConstant.USE_PASSWORD;

        // 判断传入的是否是手机号，如果是再判断是否支持该方式使用密码登录
        if (ReUtil.isMatch(RegexConstant.PHONE, requestUser.getUsername())) {
            LoginValidUtil.isSupportLoginType(Settings.LOGIN_PASSWORD_WITH_PHONE);
            loginType = LoginConstant.USE_PHONE;
        }

        // 判断传入的是否是邮箱，如果是再判断是否支持该方式使用密码登录
        if (ReUtil.isMatch(RegexConstant.EMAIL, requestUser.getUsername())) {
            LoginValidUtil.isSupportLoginType(Settings.LOGIN_PASSWORD_WITH_EMAIL);
            loginType = LoginConstant.USE_EMAIL;
        }
        return initAuthenticationToken(requestUser, loginType, true);
    }
}
