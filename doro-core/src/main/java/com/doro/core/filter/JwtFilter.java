package com.doro.core.filter;

import com.doro.common.constant.SecurityConstant;
import com.doro.core.service.setting.G_Setting;
import com.doro.core.service.setting.GlobalSettingAcquire;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (fromGateway(request)) {
            // TODO JWT 过滤
        }
        filterChain.doFilter(request, response);
    }

    private boolean fromGateway(HttpServletRequest request) {
        String from = request.getHeader(SecurityConstant.GATEWAY_HEAD);
        String gatewayHeader = GlobalSettingAcquire.get(G_Setting.GATEWAY_HEADER);
        return gatewayHeader.equals(from);
    }
}
