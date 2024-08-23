package com.doro.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一响应枚举
 *
 * @author jiage
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum {

    SUCCESS(1, "请求成功"),
    ERROR(0, "请求失败"),
    ;

    /**
     * 响应码
     */
    private final int code;

    /**
     * 响应消息
     */
    private final String message;
}
