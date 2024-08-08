package com.doro.core.service.login.provider;

import com.doro.core.service.login.LoadUser;
import com.doro.core.service.login.authentication.UsePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 认证
 */
public class UsePasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsePasswordAuthenticationProvider(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsePasswordAuthenticationToken token = (UsePasswordAuthenticationToken) authentication;

        String username = (String) token.getPrincipal();
        LoadUser user = (LoadUser) userDetailsService.loadUserByUsername(username);

        if (!token.withoutPassword()) {
            validPassword(token.getCredentials().toString(), user.getPassword());
        }

        // TODO 添加权限
        token = new UsePasswordAuthenticationToken(username, null, null);
        token.setDetails(user.getId());
        return token;
    }

    /**
     * 密码校验
     *
     * @param presentPassword 本次请求传入的密码
     * @param password        用户实际的密码
     */
    private void validPassword(String presentPassword, String password) {
        if (!bCryptPasswordEncoder.matches(presentPassword, password)) {
            throw new BadCredentialsException("密码错误");
        }
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(UsePasswordAuthenticationToken.class);
    }
}
