package com.doro.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatusEnum {

    /**
     * 未激活
     */
    NOT_ACTIVATED(0),
    /**
     * 已激活，正常状态
     */
    ACTIVATED(1),
    /**
     *
     */
    BLOCKED(2),
    ;

    private final int status;
}
