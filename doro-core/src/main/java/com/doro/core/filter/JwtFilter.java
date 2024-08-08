package com.doro.core.filter;

import com.doro.common.constant.SecurityConstant;
import com.doro.core.properties.CommonSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    CommonSecurityProperties commonSecurityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (fromGateway(request)) {

        }
        filterChain.doFilter(request, response);
    }

    private boolean fromGateway(HttpServletRequest request) {
        String gatewayHeader = request.getHeader(SecurityConstant.GATEWAY_HEAD);
        return commonSecurityProperties.getGatewayHeader().equals(gatewayHeader);
    }
}
