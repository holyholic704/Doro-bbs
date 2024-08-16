package com.doro.cache.properties;

import com.doro.common.enumeration.CacheTypeEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.concurrent.TimeUnit;

/**
 * @author jiage
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
public class CacheProperties {

    private String area = CacheDefaultProperties.DEFAULT_AREA;

    private String key;

    private long expire = CacheDefaultProperties.DEFAULT_EXPIRE;

    private long localExpire = CacheDefaultProperties.DEFAULT_EXPIRE;

    private TimeUnit unit = TimeUnit.SECONDS;

    private CacheTypeEnum cacheType = CacheTypeEnum.REMOTE;

    private boolean syncLocal = CacheDefaultProperties.DEFAULT_SYNC_LOCAL;

    private boolean cacheNullValue = CacheDefaultProperties.DEFAULT_CACHE_NULL_VALUE;

    private long localMaxSize = CacheDefaultProperties.DEFAULT_LOCAL_MAX_SIZE;
}
