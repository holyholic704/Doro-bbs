package com.doro.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

@Getter
@AllArgsConstructor
public enum SettingTypeEnum {

    BOOLEAN("Boolean", Boolean::parseBoolean),
    LONG("Long", Long::parseLong),
    DOUBLE("Double", Double::parseDouble),
    STRING("String", v -> v),
    ;

    private final String type;

    private final Function<String, ?> function;
}
