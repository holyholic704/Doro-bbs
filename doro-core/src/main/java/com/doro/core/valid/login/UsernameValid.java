package com.doro.core.valid.login;

import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;

/**
 * 用户名校验
 *
 * @author jiage
 */
public class UsernameValid extends AbstractLoginValid {

    @Override
    public MyAuthenticationToken valid(RequestUser requestUser) {
        validUsername(requestUser.getUsername());
        return null;
    }

}
