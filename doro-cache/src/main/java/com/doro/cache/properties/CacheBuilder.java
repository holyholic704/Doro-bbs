package com.doro.cache.properties;

import com.doro.common.constant.CacheConstant;
import com.doro.common.enumeration.CacheTypeEnum;
import com.doro.common.enumeration.ReferenceTypeEnum;

import java.util.concurrent.TimeUnit;

/**
 * @author jiage
 */
public class CacheBuilder {

    private String area = CacheConstant.CACHE_DEFAULT_AREA;

    private long expire = CacheConstant.CACHE_DEFAULT_EXPIRE;

    private long localExpire = this.expire;

    private TimeUnit unit = TimeUnit.SECONDS;

    private CacheTypeEnum cacheType = CacheTypeEnum.REMOTE;

    private boolean syncLocal;

    private long maxSize = CacheConstant.CACHE_DEFAULT_LOCAL_MAX_SIZE;

    private boolean expireAfterAccess;

    private ReferenceTypeEnum referenceType;

    private CacheBuilder() {
    }

    public static CacheBuilder init() {
        return new CacheBuilder();
    }

    public static CacheBuilder init(String area) {
        return new CacheBuilder()
                .setArea(area);
    }

    private CacheBuilder setArea(String area) {
        this.area = area;
        return this;
    }

    public CacheBuilder expire(long expire) {
        return expire(expire, this.unit);
    }

    public CacheBuilder expire(long expire, TimeUnit unit) {
        this.expire = expire;
        this.unit = unit;
        return this;
    }

    public CacheBuilder localExpire(long localExpire, TimeUnit unit) {
        this.localExpire = localExpire;
        this.unit = unit;
        return this;
    }

    public CacheBuilder cacheType(CacheTypeEnum cacheType) {
        this.cacheType = cacheType;
        return this;
    }

    public CacheBuilder maxSize(long maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public CacheBuilder syncLocal(boolean syncLocal) {
        this.syncLocal = syncLocal;
        return this;
    }

    public CacheBuilder expireAfterAccess(boolean expireAfterAccess) {
        this.expireAfterAccess = expireAfterAccess;
        return this;
    }

    public CacheBuilder referenceType(ReferenceTypeEnum referenceType) {
        this.referenceType = referenceType;
        return this;
    }

    public CacheProperties build() {
        return new CacheProperties()
                .setArea(this.area)
                .setExpire(this.expire)
                .setLocalExpire(this.localExpire)
                .setUnit(this.unit)
                .setCacheType(this.cacheType)
                .setSyncLocal(this.syncLocal)
                .setMaxSize(this.maxSize)
                .setExpireAfterAccess(this.expireAfterAccess)
                .setReferenceType(this.referenceType);
    }
}
