package com.doro.core.service.login.valid;

import cn.hutool.core.util.ReUtil;
import com.doro.common.constant.LoginConstant;
import com.doro.common.constant.RegexConstant;
import com.doro.common.constant.Settings;
import com.doro.core.exception.MyAuthenticationException;
import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;

/**
 * 邮箱登录校验
 */
public class EmailValidAndInitChainHandler extends AbstractValidAndInitChainHandler {

    /**
     * 校验逻辑
     */
    @Override
    protected MyAuthenticationToken handle(RequestUser requestUser) {
        if (LoginConstant.USE_EMAIL.equals(requestUser.getLoginType())) {
            this.isSupportLoginType(Settings.LOGIN_SUPPORT_EMAIL);
            this.validEmail(requestUser.getUsername());
            return initAuthenticationToken(requestUser, LoginConstant.USE_EMAIL, false);
        }
        return null;
    }

    /**
     * 邮箱校验
     *
     * @param email 邮箱
     */
    private void validEmail(String email) {
        if (!ReUtil.isMatch(RegexConstant.EMAIL, email)) {
            throw new MyAuthenticationException("邮箱格式错误");
        }
    }
}
