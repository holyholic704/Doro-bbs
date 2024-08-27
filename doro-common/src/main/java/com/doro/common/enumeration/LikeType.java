package com.doro.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jiage
 */
@Getter
@AllArgsConstructor
public enum LikeType {

    POST((short) 0),
    COMMENT((short) 1);

    private final short type;
}
