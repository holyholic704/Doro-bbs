package com.doro.common.constant;

/**
 * 分布式锁及缓存相关常量
 *
 * @author jiage
 */
public class CacheConstant {

    /**
     * 分布式锁前缀
     */
    public static final String LOCK_PREFIX = "LOCK_";

    /**
     * 分布式公平锁前缀
     */
    public static final String LOCK_FAIR_PREFIX = "LOCK_FAIR_";

    /**
     * 分布式读写锁前缀
     */
    public static final String LOCK_RW_PREFIX = "LOCK_RW_";

    /**
     * 缓存前缀
     */
    public static final String CACHE_PREFIX = "CACHE_";

    public static final String CACHE_DEFAULT_AREA = "DEFAULT_AREA";
    public static final String CACHE_SYNC = "CACHE_SYNC";
    public static final String CACHE_PROPERTIES = "CACHE_PROPERTIES";
    public static final long CACHE_DEFAULT_EXPIRE = 15 * 60;
    public static final long CACHE_DEFAULT_LOCAL_MAX_SIZE = 100;

    /**
     * 全局配置初始化
     */
    public static final String INIT_GLOBAL_SETTING = "INIT_GLOBAL_SETTING";

    public static final String ENUM_RESPONSE_CACHE = "ENUM_RESPONSE_CACHE";

    public static final String DEL_LOCAL_CACHE = "DEL_LOCAL_CACHE";

}
