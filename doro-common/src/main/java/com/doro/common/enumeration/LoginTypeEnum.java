package com.doro.common.enumeration;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LoginTypeEnum {

    USE_PASSWORD("pwd"),
    USE_PHONE("phone"),
    USE_EMAIL("email");

    private final String type;

//    public static LoginTypeEnum getByType(String loginType) {
//
//    }
}
