package com.doro.core.config;

import com.doro.core.filter.JwtFilter;
import com.doro.core.service.login.details.UseCodeUserDetails;
import com.doro.core.service.login.provider.PhoneAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@SuppressWarnings("deprecation")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UseCodeUserDetails useCodeUserDetails;
    @Autowired
    private JwtFilter jwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/test")
                .permitAll()
                .anyRequest()
                .authenticated();
        // 添加 JWT 过滤器
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    PhoneAuthenticationProvider phoneAuthenticationProvider() {
        return new PhoneAuthenticationProvider(useCodeUserDetails);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 如果实现了多个 UserDetailsService，需指定哪个 Provider 用的是哪个 UserDetailsService
        auth.authenticationProvider(phoneAuthenticationProvider())
                .userDetailsService(useCodeUserDetails);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
