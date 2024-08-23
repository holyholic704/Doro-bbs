package com.doro.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 失败消息
 *
 * @author jiage
 */
@Getter
@AllArgsConstructor
public enum ErrorMessage {

    SYSTEM_ERROR("系统异常"),
    ;

    /**
     * 错误消息
     */
    private final String message;
}
