package com.doro.cache.properties;

import com.doro.common.enumeration.ReferenceTypeEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.concurrent.TimeUnit;

/**
 * 本地缓存配置
 *
 * @author jiage
 */
@Getter
@Setter
@Accessors(chain = true)
public class LocalCacheProperties {

    /**
     * 默认配置
     */
    public static final LocalCacheProperties NORMAL = new LocalCacheProperties();

    /**
     * 超时时间
     */
    private Long expireTime;

    /**
     * 时间单位
     */
    private TimeUnit unit = TimeUnit.SECONDS;

    /**
     * 最大容量
     */
    private Long maxSize = 512L;

    /**
     * 开始计算超时时间的时机，设定了超时时间才有效
     * 默认为最后一次写入，设置为 true，表示最后一次读或写
     */
    private boolean expireAfterAccess;

    /**
     * 值的引用类型
     */
    private ReferenceTypeEnum referenceType;


    public static LocalCacheProperties normal() {
        return new LocalCacheProperties();
    }

    public static LocalCacheProperties expire(Long expireTime) {
        return normal().setExpireTime(expireTime);
    }

    public static LocalCacheProperties expire(Long expireTime, TimeUnit unit, boolean expireAfterAccess) {
        return normal()
                .setExpireTime(expireTime)
                .setUnit(unit)
                .setExpireAfterAccess(expireAfterAccess);
    }

    public static LocalCacheProperties maxSize(Long maxSize) {
        return normal().setMaxSize(maxSize);
    }

    public static LocalCacheProperties of(Long expireTime, TimeUnit unit, Long maxSize, boolean expireAfterAccess, ReferenceTypeEnum referenceType) {
        return normal()
                .setExpireTime(expireTime)
                .setUnit(unit)
                .setMaxSize(maxSize)
                .setExpireAfterAccess(expireAfterAccess)
                .setReferenceType(referenceType);
    }
}
