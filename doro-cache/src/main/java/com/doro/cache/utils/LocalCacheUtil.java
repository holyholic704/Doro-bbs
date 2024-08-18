package com.doro.cache.utils;

import com.doro.common.constant.CacheConstant;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

/**
 * 缓存工具包
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

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        return (T) LOCAL_CACHE.getIfPresent(key);
    }

    public static void put(String key, Object value) {
        LOCAL_CACHE.put(key, value);
    }

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

    public static void remove(String key) {
        LOCAL_CACHE.invalidate(key);
    }
}
