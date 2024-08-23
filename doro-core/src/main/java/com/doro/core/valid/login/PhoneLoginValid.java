package com.doro.core.valid.login;

import com.doro.common.constant.LoginConstant;
import com.doro.core.service.login.provider.MyAuthenticationToken;
import com.doro.core.service.setting.G_Setting;
import com.doro.core.service.setting.GlobalSettingAcquire;
import com.doro.orm.request.RequestUser;

/**
 * 使用手机登录校验
 *
 * @author jiage
 */
public final class PhoneLoginValid extends AbstractLoginValid {

    @Override
    public MyAuthenticationToken valid(RequestUser requestUser) {
        if (LoginConstant.USE_PHONE.equals(requestUser.getLoginType())) {
            isSupportLoginType(GlobalSettingAcquire.get(G_Setting.LOGIN_SUPPORT_PHONE));
            validPhone(requestUser.getUsername());
            return initAuthenticationToken(requestUser, LoginConstant.USE_PHONE, false);
        }
        return null;
    }
}
