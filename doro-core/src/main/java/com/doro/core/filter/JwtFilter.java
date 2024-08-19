package com.doro.core.filter;

import com.doro.common.constant.SecurityConstant;
import com.doro.core.service.setting.CommonSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 过滤器
 *
 * @author jiage
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final CommonSecurityProperties commonSecurityProperties;

    @Autowired
    public JwtFilter(CommonSecurityProperties commonSecurityProperties) {
        this.commonSecurityProperties = commonSecurityProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (fromGateway(request)) {
            // TODO JWT 过滤
        }
        filterChain.doFilter(request, response);
    }

    private boolean fromGateway(HttpServletRequest request) {
        String gatewayHeader = request.getHeader(SecurityConstant.GATEWAY_HEAD);
        return commonSecurityProperties.getGatewayHeader().equals(gatewayHeader);
    }
}
