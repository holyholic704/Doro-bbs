package com.doro.cache.utils;

import com.doro.cache.processor.DelMessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 二级缓存工具类
 *
 * @author jiage
 */
@Component
public class MultiCacheUtil {

    private static DelMessageProcessor delMessageProcessor;

    @Autowired
    public void setDelMessageProcessor(DelMessageProcessor delMessageProcessor) {
        MultiCacheUtil.delMessageProcessor = delMessageProcessor;
    }

    /**
     * 获取缓存
     */
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

    /**
     * 删除缓存
     */
    public static void remove(String key) {
        RemoteCacheUtil.remove(key);
        removeLocalAndPublish(key);
    }

    /**
     * 添加缓存
     */
    public static void put(String key, Object value) {
        RemoteCacheUtil.put(key, value);
        removeLocalAndPublish(key);
    }

    /**
     * 添加缓存，可自定义超时时间
     */
    public static void put(String key, Object value, Duration duration) {
        RemoteCacheUtil.put(key, value, duration);
        removeLocalAndPublish(key);
    }

    /**
     * 没有缓存则添加，有则直接返回
     *
     * @return 没有则返回当前值，有则返回缓存中的值
     */
    public static <T> T putIfAbsent(String key, T value, Duration duration) {
        T existValue = RemoteCacheUtil.putIfAbsent(key, value, duration);
        if (existValue == value) {
            removeLocalAndPublish(key);
        }
        return existValue;
    }

    /**
     * 删除本地缓存，并发送删除消息
     */
    private static void removeLocalAndPublish(String key) {
        LocalCacheUtil.remove(key);
        delMessageProcessor.send(key);
    }
}
