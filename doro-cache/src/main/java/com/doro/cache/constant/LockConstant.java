package com.doro.cache.constant;

/**
 * 分布式锁相关常量
 *
 * @author jiage
 */
public class LockConstant {

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
     * 全局配置初始化
     */
    public static final String INIT_GLOBAL_SETTING = "INIT_GLOBAL_SETTING";
}
