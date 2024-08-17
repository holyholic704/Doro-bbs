package com.doro.cache.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Function;

/**
 * 缓存工具包
 *
 * @author jiage
 */
@Component
@DependsOn("lockUtil")
public class LocalCacheUtil {

    /**
     * 本地缓存集合
     */
    private static final Cache<String, Object> LOCAL_CACHE = Caffeine.newBuilder()
            .maximumSize(1024)
            .expireAfterAccess(Duration.ofMinutes(15))
            .build();

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        return (T) LOCAL_CACHE.getIfPresent(key);
    }

    public static void put(String key, Object value) {
        LOCAL_CACHE.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T computeIfAbsent(String key, Function<String, T> function) {
        Object existValue = LOCAL_CACHE.getIfPresent(key);
        if (existValue != null) {
            return (T) existValue;
        } else {
            T value = function.apply(key);
            LOCAL_CACHE.put(key, value);
            return value;
        }
    }

    public static void remove(String key) {
        LOCAL_CACHE.invalidate(key);
    }
}
