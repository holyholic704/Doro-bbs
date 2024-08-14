package com.doro.cache.properties;

import com.doro.common.enumeration.ReferenceTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.concurrent.TimeUnit;

/**
 * 本地缓存配置
 */
@Data
@Accessors(chain = true)
public class LocalCacheProperties {

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
}
