package com.doro.core.properties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

/**
 * @author jiage
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum G_Setting {

    VERSION(0, Integer::parseInt),
    LOGIN_SUPPORT_EMAIL(false, Boolean::parseBoolean),
    LOGIN_SUPPORT_PHONE(false, Boolean::parseBoolean),
    LOGIN_PASSWORD_WITH_EMAIL(false, Boolean::parseBoolean),
    LOGIN_PASSWORD_WITH_PHONE(false, Boolean::parseBoolean),
    REGISTER_SUPPORT_EMAIL(false, Boolean::parseBoolean),
    REGISTER_SUPPORT_PHONE(false, Boolean::parseBoolean),
    USER_NEED_ACTIVE(false, Boolean::parseBoolean),
    JWT_HEADER("email", v -> v),
    JWT_SECRET("dafengqixiyunfeiyangweijiahaineixiguiguxiangandemengshixishousifang", v -> v),
    JWT_EXPIRATION(60 * 60 * 24, Long::parseLong),
    ;

    private final Object defaultValue;

    private final Function<String, ?> function;

}
