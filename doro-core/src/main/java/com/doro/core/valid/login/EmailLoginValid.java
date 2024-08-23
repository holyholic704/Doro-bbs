package com.doro.core.valid.login;

import com.doro.common.constant.LoginConstant;
import com.doro.orm.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;
import com.doro.core.service.setting.G_Setting;
import com.doro.core.service.setting.GlobalSettingAcquire;

/**
 * 使用邮箱登录校验
 *
 * @author jiage
 */
public class EmailLoginValid extends AbstractLoginValid {

    @Override
    public MyAuthenticationToken valid(RequestUser requestUser) {
        if (LoginConstant.USE_EMAIL.equals(requestUser.getLoginType())) {
            isSupportLoginType(GlobalSettingAcquire.get(G_Setting.LOGIN_SUPPORT_EMAIL));
            validEmail(requestUser.getUsername());
            return initAuthenticationToken(requestUser, LoginConstant.USE_EMAIL, false);
        }
        return null;
    }
}
