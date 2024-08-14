package com.doro.core.service.login.valid;

import com.doro.common.constant.LoginConstant;
import com.doro.core.properties.GlobalSettingTemplate;
import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;
import com.doro.core.utils.LoginValidUtil;

/**
 * 使用邮箱登录校验
 */
public class EmailLoginValid implements LoginValid<RequestUser, MyAuthenticationToken> {

    @Override
    public MyAuthenticationToken test(RequestUser requestUser) {
        if (LoginConstant.USE_EMAIL.equals(requestUser.getLoginType())) {
            LoginValidUtil.isSupportLoginType(GlobalSettingTemplate.LOGIN_SUPPORT_EMAIL);
            LoginValidUtil.validEmail(requestUser.getUsername());
            return initAuthenticationToken(requestUser, LoginConstant.USE_EMAIL, false);
        }
        return null;
    }
}
