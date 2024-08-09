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
}
