package com.doro.cache.anno;

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
     * 键
     */
    String key();

    /**
     * 超时时间
     */
    long expire() default 15;

    /**
     * 时间单位
     */
    TimeUnit unit() default TimeUnit.MINUTES;

    /**
     * 缓存类型
     */
    CacheTypeEnum cacheType() default CacheTypeEnum.REMOTE;

}
