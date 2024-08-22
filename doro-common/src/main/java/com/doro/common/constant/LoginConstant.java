package com.doro.common.constant;

/**
 * 登录注册相关常量
 *
 * @author jiage
 */
public class LoginConstant {

    /**
     * 登录方式：使用密码
     */
    public static final String USE_PASSWORD = "pwd";

    /**
     * 登录方式：使用手机号
     */
    public static final String USE_PHONE = "phone";

    /**
     * 登录方式：使用邮箱
     */
    public static final String USE_EMAIL = "email";

    /**
     * 用户 ID
     */
    public static final String CLAIMS_USER_ID = "USER_ID";

    /**
     * JWT 标签
     */
    public static final String JWT_HEADER = "Authorization";

    /**
     * JWT 前缀
     */
    public static final String JWT_SUFFIX = "Bearer";
}
