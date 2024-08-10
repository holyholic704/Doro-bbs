package com.doro.core.service.login.valid;

import cn.hutool.core.util.ReUtil;
import com.doro.common.constant.LoginConstant;
import com.doro.common.constant.RegexConstant;
import com.doro.common.constant.Settings;
import com.doro.core.exception.MyAuthenticationException;
import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;

public class PhoneValidAndInitChainHandler extends AbstractValidAndInitChainHandler {

    @Override
    protected MyAuthenticationToken handle(RequestUser requestUser) {
        if (LoginConstant.USE_PHONE.equals(requestUser.getLoginType())) {
            this.isSupportLoginType(Settings.LOGIN_SUPPORT_PHONE);
            this.validPhone(requestUser.getUsername());
            return initAuthenticationToken(requestUser, LoginConstant.USE_PHONE, false);
        }
        return null;
    }

    private void validPhone(String phone) {
        if (!ReUtil.isMatch(RegexConstant.PHONE, phone)) {
            throw new MyAuthenticationException("手机号码格式错误");
        }
    }
}
