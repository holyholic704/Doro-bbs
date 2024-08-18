package com.doro.cache.utils;

import com.doro.common.constant.CacheConstant;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

/**
 * 本地缓存工具类
 *
 * @author jiage
 */
@Component
public class LocalCacheUtil {

    /**
     * 本地缓存
     */
    private static final Cache<String, Object> LOCAL_CACHE = Caffeine.newBuilder()
            .maximumSize(CacheConstant.CACHE_DEFAULT_MAX_SIZE)
            .expireAfterAccess(CacheConstant.CACHE_DEFAULT_DURATION)
            .build();

    /**
     * 获取缓存
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        return (T) LOCAL_CACHE.getIfPresent(key);
    }

    /**
     * 添加缓存
     */
    public static void put(String key, Object value) {
        LOCAL_CACHE.put(key, value);
    }

    /**
     * 没有缓存则添加，有则直接返回
     *
     * @return 没有则返回当前值，有则返回缓存中的值
     */
    @SuppressWarnings("unchecked")
    public static <T> T putIfAbsent(String key, T value) {
        Object existValue = LOCAL_CACHE.getIfPresent(key);
        if (existValue != null) {
            return (T) existValue;
        } else {
            LOCAL_CACHE.put(key, value);
            return value;
        }
    }

    /**
     * 删除缓存
     */
    public static void remove(String key) {
        LOCAL_CACHE.invalidate(key);
    }
}
