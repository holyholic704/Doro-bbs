package com.doro.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LoginTypeEnum {

    USE_PASSWORD("pwd"),
    USE_PHONE("phone"),
    USE_EMAIL("email");

    private final String type;

    public static LoginTypeEnum getByType(String loginType) {
        for (LoginTypeEnum loginTypeEnum : LoginTypeEnum.values()) {
            if (loginTypeEnum.getType().equals(loginType)) {
                return loginTypeEnum;
            }
        }
        return LoginTypeEnum.USE_PASSWORD;
    }
}
