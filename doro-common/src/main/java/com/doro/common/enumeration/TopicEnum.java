package com.doro.common.enumeration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jiage
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TopicEnum {

    UPDATE_COUNT("UPDATE_COUNT", "UPDATE_COUNT_GROUP"),
    BATCH_SAVE("BATCH_SAVE", "BATCH_SAVE_GROUP"),
    LIKE_SAVE("LIKE_SAVE", "LIKE_SAVE_GROUP"),
    ;

    private final String topic;

    private final String consumerGroup;
}
