package com.doro.mq.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jiage
 */
@Getter
@AllArgsConstructor
public class CountMqModel {

    private String cachePrefix;

    private long id;

    private Long count;
}
