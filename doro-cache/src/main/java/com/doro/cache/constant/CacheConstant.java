package com.doro.cache.constant;

import java.time.Duration;

/**
 * 缓存相关常量
 *
 * @author jiage
 */
public class CacheConstant {

    /**
     * 缓存前缀
     */
    public static final String CACHE_PREFIX = "CACHE_";

    /**
     * 默认缓存超时时长
     */
    public static final Duration CACHE_DEFAULT_DURATION = Duration.ofMinutes(15);

    /**
     * 默认缓存容量
     */
    public static final long CACHE_DEFAULT_MAX_SIZE = 1024;

    /**
     * 全局配置缓存
     */
    public static final String GLOBAL_SETTING = "GLOBAL_SETTING";

    /**
     * 删除本地缓存的主题
     */
    public static final String DEL_LOCAL_CACHE_TOPIC = "DEL_LOCAL_CACHE";

}
