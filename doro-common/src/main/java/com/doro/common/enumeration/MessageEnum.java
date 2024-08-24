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
public enum MessageEnum {

    SYSTEM_ERROR("系统异常"),
    SAVE_SUCCESS("保存成功"),
    SAVE_ERROR("保存失败"),
    NO_DATA_ERROR("没有数据啊"),
    ;

    /**
     * 错误消息
     */
    private final String message;
}
