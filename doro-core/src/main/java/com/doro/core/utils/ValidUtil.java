package com.doro.core.utils;

/**
 * @author jiage
 */
public class ValidUtil {

    public static boolean validStrLength(String str, int minLen, int maxLen, String errorMessage) {
        return str != null && str.length() >= minLen && str.length() <= maxLen;
    }
}
