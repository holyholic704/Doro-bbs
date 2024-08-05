package com.doro.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一响应枚举
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum {

    /**
     * 为了与 HTTP 状态码作区分，规定范围为 10000 ~ 99999
     */
    SUCCESS(10001, "请求成功"),
    ERROR(10000, "请求失败"),
    ;

    /**
     * 状态码
     */
    private final int code;

    /**
     * 响应消息
     */
    private final String message;
}
