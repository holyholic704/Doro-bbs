package com.doro.cache.utils;

import java.time.Duration;

/**
 * @author jiage
 */
public class MultiCacheUtil {

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        Object value = LocalCacheUtil.get(key);
        if (value != null) {
            return (T) value;
        }

        T remoteCache = RemoteCacheUtil.get(key);
        if (remoteCache != null) {
            LocalCacheUtil.put(key, remoteCache);
        }
        return remoteCache;
    }

    public static void remove(String key) {
        RemoteCacheUtil.remove(key);
        LocalCacheUtil.remove(key);
    }

    public static void put(String key, Object value, Duration duration) {
        RemoteCacheUtil.put(key, value, duration);
        LocalCacheUtil.remove(key);
    }

    public static <T> T putIfAbsent(String key, T value, Duration duration) {
        T existValue = RemoteCacheUtil.putIfAbsent(key, value, duration);
        if (existValue == value) {
            LocalCacheUtil.remove(key);
        }
        return existValue;
    }
}
