package com.doro.core.service.login;

import com.doro.common.constant.LoginConstant;
import com.doro.core.model.request.RequestUser;
import com.doro.core.model.response.ResponseUser;
import com.doro.core.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service(value = LoginConstant.USE_PASSWORD)
public class UsernamePasswordLoginService extends BaseLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @Override
    public AbstractAuthenticationToken getAuthenticationToken(RequestUser requestUser) {
        return new UsernamePasswordAuthenticationToken(requestUser.getUsername(), requestUser.getPassword());
    }

    @Override
    public ResponseUser initToken(Authentication authentication, RequestUser requestUser) {
        return null;
    }
}
