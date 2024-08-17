package com.doro.cache.properties;

import com.doro.common.enumeration.CacheTypeEnum;
import com.doro.common.enumeration.ReferenceTypeEnum;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.concurrent.TimeUnit;

/**
 * @author jiage
 */
@Getter
@ToString
@Accessors(chain = true)
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class CacheProperties {

    private String area;

    private long expire;

    private long localExpire;

    private TimeUnit unit;

    private CacheTypeEnum cacheType;

    private boolean syncLocal;

    private long maxSize;

    private boolean expireAfterAccess;

    private ReferenceTypeEnum referenceType;
}
