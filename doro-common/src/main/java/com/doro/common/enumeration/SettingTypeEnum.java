package com.doro.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

@Getter
@AllArgsConstructor
public enum SettingTypeEnum {

    BOOLEAN("boolean", Boolean::parseBoolean),
    BYTE("byte", Byte::parseByte),
    SHORT("short", Short::parseShort),
    INTEGER("integer", Integer::parseInt),
    LONG("long", Long::parseLong),
    FLOAT("float", Float::parseFloat),
    DOUBLE("double", Double::parseDouble),
    CHAR("character", v -> v.charAt(0)),
    STRING("string", v -> v),
    ;

    private final String type;

    private final Function<String, ?> function;
}
