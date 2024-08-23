package com.doro.core.config;

import com.doro.core.filter.RequestFilter;
import com.doro.core.service.login.provider.MyAuthenticationProvider;
import com.doro.core.service.login.provider.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security 配置
 *
 * @author jiage
 */
@Configuration
@SuppressWarnings("deprecation")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RequestFilter requestFilter;

    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl, RequestFilter requestFilter) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.requestFilter = requestFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/login", "/register")
                .permitAll()
                .anyRequest()
                .authenticated();
        // 添加 JWT 过滤器
        http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    MyAuthenticationProvider myAuthenticationProvider() {
        return new MyAuthenticationProvider(bCryptPasswordEncoder, userDetailsServiceImpl);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        // 自定义认证方式
        auth.authenticationProvider(myAuthenticationProvider());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }
}
