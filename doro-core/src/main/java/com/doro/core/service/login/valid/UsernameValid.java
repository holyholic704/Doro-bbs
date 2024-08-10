package com.doro.core.service.login.valid;

import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;
import com.doro.core.utils.LoginValidUtil;

/**
 * 用户名校验
 */
public class UsernameValid implements LoginValid<RequestUser, MyAuthenticationToken> {

    @Override
    public MyAuthenticationToken test(RequestUser requestUser) {
        LoginValidUtil.validUsername(requestUser.getUsername());
        return null;
    }
}
