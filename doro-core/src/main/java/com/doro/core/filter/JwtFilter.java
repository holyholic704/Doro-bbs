//package com.doro.core.filter;
//
//import com.doro.common.constant.SecurityConstant;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//@RefreshScope
//public class JwtFilter extends OncePerRequestFilter {
//
//    @Value("${common-security.gateway-header}")
//    private String gatewayHeader;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        if (fromGateway(request)) {
//        }
//    }
//
//    private boolean fromGateway(HttpServletRequest request) {
//        String gatewayHeader = request.getHeader(SecurityConstant.GATEWAY_HEAD);
//        return this.gatewayHeader.equals(gatewayHeader);
//    }
//}
