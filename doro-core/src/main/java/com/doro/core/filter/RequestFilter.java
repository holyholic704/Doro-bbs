package com.doro.core.filter;

import cn.hutool.core.util.StrUtil;
import com.doro.cache.utils.RemoteCacheUtil;
import com.doro.common.constant.CacheConstant;
import com.doro.common.constant.LoginConstant;
import com.doro.common.constant.SecurityConstant;
import com.doro.core.service.login.provider.MyAuthenticationToken;
import com.doro.core.service.setting.G_Setting;
import com.doro.core.service.setting.GlobalSettingAcquire;
import com.doro.core.utils.IpUtil;
import com.doro.core.utils.JwtUtil;
import com.doro.core.utils.UserUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class RequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println(IpUtil.getIp(request));
        System.out.println(UserUtil.getUsername());
//        if (fromGateway(request) && checkToken(request)) {
        if (checkToken(request)) {
            // TODO 网关过滤
        }
        System.out.println(UserUtil.getUsername());
        filterChain.doFilter(request, response);
    }

    private boolean checkToken(HttpServletRequest request) {
        String token = request.getHeader(LoginConstant.JWT_HEADER);
        if (StrUtil.isNotEmpty(token) && StrUtil.startWith(token, LoginConstant.JWT_SUFFIX)) {
            token = token.substring(LoginConstant.JWT_SUFFIX.length()).trim();

            Claims claims = JwtUtil.getPayload(token);
            String username = JwtUtil.getUsername(claims);
            String storeToken = RemoteCacheUtil.get(CacheConstant.JWT_PREFIX + username);

            // 传入的 Token 是否与系统存储的相同，是否
            if (token.equals(storeToken) && !JwtUtil.isExpired(token)) {
                // TODO 添加权限列表?
                MyAuthenticationToken authenticationToken = new MyAuthenticationToken(username, null, null);
                authenticationToken.setDetails(JwtUtil.getUserId(claims));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                return true;
            }
        }
        return false;
    }

    private boolean fromGateway(HttpServletRequest request) {
        String from = request.getHeader(SecurityConstant.GATEWAY_HEAD);
        String gatewayHeader = GlobalSettingAcquire.get(G_Setting.GATEWAY_HEADER);
        return gatewayHeader.equals(from);
    }
}
