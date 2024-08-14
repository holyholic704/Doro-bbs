package com.doro.core.properties;

/**
 * 全局配置模版
 */
public class GlobalSettingTemplate {

    public static int VERSION = 0;

    public static boolean LOGIN_SUPPORT_EMAIL = false;

    public static boolean LOGIN_SUPPORT_PHONE = false;

    public static boolean LOGIN_PASSWORD_WITH_EMAIL = false;

    public static boolean LOGIN_PASSWORD_WITH_PHONE = false;

    public static boolean REGISTER_SUPPORT_EMAIL = false;

    public static boolean REGISTER_SUPPORT_PHONE = false;

    public static boolean USER_NEED_ACTIVE = false;

    public static final String JWT_HEADER = "email";

    public static final String JWT_SECRET = "dafengqixiyunfeiyangweijiahaineixiguiguxiangandemengshixishousifang";

    public static final Integer JWT_EXPIRED = 60 * 60 * 24;
}
