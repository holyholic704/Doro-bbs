package com.doro.common.enumeration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jiage
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CommentType {

    PARENT("系统异常"),
    SUB("保存成功"),
    ;

    private final String type;

}
