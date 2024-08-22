package com.doro.core.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author jiage
 */
@Component
public class UserUtil {

    public static String getUsername() {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }

    public static Long getUserId() {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            return (Long) authentication.getDetails();
        }
        return null;
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
