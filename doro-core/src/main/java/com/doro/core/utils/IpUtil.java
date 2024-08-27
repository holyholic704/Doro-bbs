package com.doro.core.utils;

import cn.hutool.core.net.NetUtil;
import com.doro.common.constant.Separator;

import javax.servlet.http.HttpServletRequest;

/**
 * IP 工具类
 *
 * @author jiage
 */
public class IpUtil {

    private static final String[] IP_HEADERS = {
            "x-forwarded-for",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP"
    };

    public static String getIp(HttpServletRequest request) {
        String ip;
        if ((ip = getIpFromHeader(request)) != null || (ip = getRemoteAddr(request)) != null) {
            return getFirstIfMultiAddr(ip);
        }
        return null;
    }

    private static String getIpFromHeader(HttpServletRequest request) {
        for (String header : IP_HEADERS) {
            String ipAddr = request.getHeader(header);
            if (!NetUtil.isUnknown(ipAddr)) {
                return ipAddr;
            }
        }
        return null;
    }

    private static String getRemoteAddr(HttpServletRequest request) {
        // 本机调试时可能会出现获取到 0:0:0:0:0:0:0:1
        String ipAddr = request.getRemoteAddr();
        if (NetUtil.isInnerIP(ipAddr)) {
            return null;
        }
        return ipAddr;
    }

    private static String getFirstIfMultiAddr(String ipAddr) {
        int firstCommaIndex = ipAddr.indexOf(Separator.COMMA);
        if (firstCommaIndex > 0) {
            ipAddr = ipAddr.substring(0, firstCommaIndex);
        }
        return ipAddr;
    }
}
