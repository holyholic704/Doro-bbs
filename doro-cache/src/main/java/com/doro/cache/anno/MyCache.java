package com.doro.cache.anno;

import com.doro.cache.properties.CacheDefaultProperties;
import com.doro.common.enumeration.CacheTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author jiage
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyCache {

    /**
     * 是否使用单独的区域
     * 即本地使用单独的缓存，Redis 新建 Hash 类型的缓存
     */
    String area() default CacheDefaultProperties.DEFAULT_AREA;

    /**
     * 键
     * 如果设置了 area，表示为字段
     */
    String key();

    /**
     * 超时时间
     */
    long expire() default CacheDefaultProperties.DEFAULT_EXPIRE;

    /**
     * 本地缓存超时时间
     */
    long localExpire() default CacheDefaultProperties.DEFAULT_EXPIRE;

    /**
     * 时间单位
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 缓存类型
     */
    CacheTypeEnum cacheType() default CacheTypeEnum.REMOTE;

    /**
     * 是否同步刷新本地缓存
     */
    boolean syncLocal() default CacheDefaultProperties.DEFAULT_SYNC_LOCAL;

    /**
     * 是否存储 null 值
     */
    boolean cacheNullValue() default CacheDefaultProperties.DEFAULT_CACHE_NULL_VALUE;

    /**
     * 本地缓存最大容量
     */
    long localMaxSize() default CacheDefaultProperties.DEFAULT_LOCAL_MAX_SIZE;
}
