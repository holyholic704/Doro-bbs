package com.doro.cache.properties;

import com.doro.common.enumeration.ReferenceTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.concurrent.TimeUnit;

@Data
@Accessors(chain = true)
public class LocalCacheProperties {

    private Long expireTime;

    private TimeUnit unit = TimeUnit.SECONDS;

    private Long maxSize = 100L;

    private boolean expireAfterAccess;

    private ReferenceTypeEnum referenceType;
}
