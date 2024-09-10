package com.doro.mq.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jiage
 */
@Getter
@Setter
@AllArgsConstructor
public class CountMqModel {

    private String cachePrefix;

    private long id;

    private Long count;
}
