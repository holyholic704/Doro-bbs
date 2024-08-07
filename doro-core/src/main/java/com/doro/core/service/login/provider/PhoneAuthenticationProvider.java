package com.doro.core.service.login.provider;

import com.doro.bean.user.User;
import com.doro.core.service.login.authentication.PhoneAuthenticationToken;
import com.doro.core.service.login.details.UseCodeUserDetails;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class PhoneAuthenticationProvider implements AuthenticationProvider {

    private final UseCodeUserDetails useCodeUserDetails;

    public PhoneAuthenticationProvider(UseCodeUserDetails useCodeUserDetails) {
        this.useCodeUserDetails = useCodeUserDetails;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PhoneAuthenticationToken token = (PhoneAuthenticationToken) authentication;
        // 获取手机号
        String phoneNum = (String) token.getPrincipal();
        // 根据手机号获取用户信息
        User user = (User) useCodeUserDetails.loadUserByUsername(phoneNum);
        token.setAuthenticated(true);
        token.setDetails(user.getId());
        return authentication;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(PhoneAuthenticationToken.class);
    }
}
