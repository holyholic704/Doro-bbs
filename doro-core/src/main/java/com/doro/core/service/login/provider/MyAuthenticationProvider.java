package com.doro.core.service.login.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 自定义认证
 *
 * @author jiage
 */
public class MyAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsServiceImpl userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public MyAuthenticationProvider(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * 认证校验
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MyAuthenticationToken token = (MyAuthenticationToken) authentication;

        String username = (String) token.getPrincipal();
        MyUserDetails user = (MyUserDetails) userDetailsService.loadUserByUsername(username, token.getLoginType());

        if (token.isUsePassword()) {
            validPassword(token.getCredentials().toString(), user.getPassword());
        }

        // TODO 添加权限
        token = new MyAuthenticationToken(user.getUsername(), null, null);
        token.setDetails(user.getId());

        // 将认证信息存储在 SecurityContextHolder 中
        SecurityContextHolder.getContext().setAuthentication(token);

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
        return clazz.isAssignableFrom(MyAuthenticationToken.class);
    }
}
