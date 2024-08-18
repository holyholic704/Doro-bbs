package com.doro.cache.utils;

import com.doro.cache.processor.DelMessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author jiage
 */
@Component
public class MultiCacheUtil {

    private static DelMessageProcessor delMessageProcessor;

    @Autowired
    public void setDelMessageProcessor(DelMessageProcessor delMessageProcessor) {
        MultiCacheUtil.delMessageProcessor = delMessageProcessor;
    }

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
        removeLocalAndPublish(key);
    }

    public static void put(String key, Object value, Duration duration) {
        RemoteCacheUtil.put(key, value, duration);
        removeLocalAndPublish(key);
    }

    public static <T> T putIfAbsent(String key, T value, Duration duration) {
        T existValue = RemoteCacheUtil.putIfAbsent(key, value, duration);
        if (existValue == value) {
            removeLocalAndPublish(key);
        }
        return existValue;
    }

    private static void removeLocalAndPublish(String key) {
        LocalCacheUtil.remove(key);
        delMessageProcessor.send(key);
    }
}
