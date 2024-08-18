package com.doro.cache.utils;

import com.doro.common.constant.CacheConstant;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 远程缓存（Redis）工具类
 *
 * @author jiage
 */
@Component
public class RemoteCacheUtil {

    private static RedissonClient redissonClient;

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        RemoteCacheUtil.redissonClient = redissonClient;
    }

    /**
     * 获取缓存
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        return (T) getBucket(key).get();
    }

    /**
     * 添加缓存
     */
    public static void put(String key, Object value) {
        getBucket(key).set(value);
    }

    /**
     * 添加缓存，可自定义超时时间
     */
    public static void put(String key, Object value, Duration duration) {
        getBucket(key).set(value, duration);
    }

    /**
     * 没有缓存则添加，有则直接返回
     *
     * @return 没有则返回当前值，有则返回缓存中的值
     */
    public static <T> T putIfAbsent(String key, T value) {
        return putIfAbsent(key, value, CacheConstant.CACHE_DEFAULT_DURATION);
    }

    /**
     * 没有缓存则添加，有则直接返回，可自定义超时时间
     *
     * @return 没有则返回当前值，有则返回缓存中的值
     */
    @SuppressWarnings("unchecked")
    public static <T> T putIfAbsent(String key, T value, Duration duration) {
        RBatch rBatch = createBatch();
        rBatch.getBucket(key).getAsync();
        rBatch.getBucket(key).setIfAbsentAsync(value, duration);
        rBatch.execute();

        BatchResult<?> batchResult = rBatch.execute();
        List<?> responses = batchResult.getResponses();

        Boolean isSuccess = (Boolean) responses.get(1);
        return isSuccess != null && isSuccess ? value : (T) responses.get(0);
    }

    public static void putMap(String key, Map<String, Object> map) {
        putMap(key, map, CacheConstant.CACHE_DEFAULT_DURATION);
    }

    public static void putMap(String key, Map<String, Object> map, Duration duration) {
        RMapCache<String, Object> rMapCache = getMapCache(key);
        rMapCache.putAll(map, duration.getSeconds(), TimeUnit.SECONDS);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getByField(String key, String field) {
        RMapCache<String, Object> rMapCache = getMapCache(key);
        return (T) rMapCache.get(field);
    }

    @SuppressWarnings("unchecked")
    public static <T> Map<String, T> getAllMap(String key) {
        return (Map<String, T>) getMapCache(key).readAllMap();
    }

    /**
     * 删除缓存
     */
    public static void remove(String key) {
        getBucket(key).delete();
    }

    private static RBucket<Object> getBucket(String name) {
        return redissonClient.getBucket(name);
    }

    private static RMapCache<String, Object> getMapCache(String name) {
        return redissonClient.getMapCache(name);
    }

    private static RBatch createBatch() {
        return redissonClient.createBatch();
    }
}
