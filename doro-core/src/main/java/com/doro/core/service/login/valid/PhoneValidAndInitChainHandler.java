package com.doro.core.service.login.valid;

import com.doro.common.constant.LoginConstant;
import com.doro.common.constant.Settings;
import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;
import com.doro.core.utils.LoginValidUtil;

/**
 * 手机号登录校验
 */
public class PhoneValidAndInitChainHandler extends AbstractValidAndInitChainHandler {

    /**
     * 校验逻辑
     */
    @Override
    protected MyAuthenticationToken handle(RequestUser requestUser) {
        if (LoginConstant.USE_PHONE.equals(requestUser.getLoginType())) {
            LoginValidUtil.isSupportLoginType(Settings.LOGIN_SUPPORT_PHONE);
            LoginValidUtil.validPhone(requestUser.getUsername());
            return initAuthenticationToken(requestUser, LoginConstant.USE_PHONE, false);
        }
        return null;
    }
}
