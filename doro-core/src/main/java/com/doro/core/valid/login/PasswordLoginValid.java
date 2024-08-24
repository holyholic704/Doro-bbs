package com.doro.core.valid.login;

import cn.hutool.core.util.ReUtil;
import com.doro.common.constant.LoginConstant;
import com.doro.common.constant.Regex;
import com.doro.core.service.login.provider.MyAuthenticationToken;
import com.doro.core.service.setting.G_Setting;
import com.doro.core.service.setting.GlobalSettingAcquire;
import com.doro.orm.model.request.RequestUser;

/**
 * 使用密码登录校验
 *
 * @author jiage
 */
public final class PasswordLoginValid extends AbstractLoginValid {

    @Override
    public MyAuthenticationToken valid(RequestUser requestUser) {
        validUsername(requestUser.getUsername());
        validPassword(requestUser.getPassword());

        // 默认使用用户名密码登录
        String loginType = LoginConstant.USE_PASSWORD;

        // 判断传入的是否是手机号，如果是再判断是否支持该方式使用密码登录
        if (ReUtil.isMatch(Regex.PHONE, requestUser.getUsername())) {
            isSupportLoginType(GlobalSettingAcquire.get(G_Setting.LOGIN_PASSWORD_WITH_PHONE));
            loginType = LoginConstant.USE_PHONE;
        }

        // 判断传入的是否是邮箱，如果是再判断是否支持该方式使用密码登录
        if (ReUtil.isMatch(Regex.EMAIL, requestUser.getUsername())) {
            isSupportLoginType(GlobalSettingAcquire.get(G_Setting.LOGIN_PASSWORD_WITH_EMAIL));
            loginType = LoginConstant.USE_EMAIL;
        }
        return initAuthenticationToken(requestUser, loginType, true);
    }
}
