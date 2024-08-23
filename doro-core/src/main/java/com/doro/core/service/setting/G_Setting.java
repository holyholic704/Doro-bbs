package com.doro.core.service.setting;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

/**
 * 全局配置
 *
 * @author jiage
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum G_Setting {

    /**
     * 配置版本
     */
    VERSION(0, Integer::parseInt),

    /**
     * 是否支持邮箱登录
     */
    LOGIN_SUPPORT_EMAIL(false, Boolean::parseBoolean),

    /**
     * 是否支持手机号登录
     */
    LOGIN_SUPPORT_PHONE(false, Boolean::parseBoolean),

    /**
     * 使用密码登录时是否支持输入邮箱
     */
    LOGIN_PASSWORD_WITH_EMAIL(false, Boolean::parseBoolean),

    /**
     * 使用密码登录时是否支持输入手机号
     */
    LOGIN_PASSWORD_WITH_PHONE(false, Boolean::parseBoolean),

    /**
     * 是否支持邮箱注册
     */
    REGISTER_SUPPORT_EMAIL(false, Boolean::parseBoolean),

    /**
     * 是否支持手机号注册
     */
    REGISTER_SUPPORT_PHONE(false, Boolean::parseBoolean),

    /**
     * 注册完成后用户是否需要激活
     */
    USER_NEED_ACTIVE(false, Boolean::parseBoolean),

    /**
     * JWT 标签
     */
    JWT_HEADER("dorodorodoro", v -> v),

    /**
     * JWT 秘钥
     */
    JWT_SECRET("dafengqixiyunfeiyangweijiahaineixiguiguxiangandemengshixishousifang", v -> v),

    /**
     * JWT 超时时间
     */
    JWT_EXPIRATION(60 * 60 * 24, Integer::parseInt),

    /**
     * Gateway 携带标签
     */
    GATEWAY_HEADER("lumanmanqixiuyuanxi", v -> v),
    ;

    /**
     * 默认值
     */
    private final Object defaultValue;

    /**
     * 数据类型转换，将字符串转为相应的类型
     */
    private final Function<String, ?> convert;

}
