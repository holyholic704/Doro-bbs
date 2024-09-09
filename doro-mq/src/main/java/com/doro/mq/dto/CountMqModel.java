package com.doro.mq.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author jiage
 */
@Getter
@AllArgsConstructor
public class CountMqModel implements Serializable {

    private String cachePrefix;

    private long id;

    private Long count;
}
