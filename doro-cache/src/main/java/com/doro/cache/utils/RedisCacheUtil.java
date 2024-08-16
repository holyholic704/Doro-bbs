package com.doro.cache.utils;

import org.redisson.api.RBucket;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jiage
 */
@Component
public class RedisCacheUtil {

    private static RedissonClient redissonClient;

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        RedisCacheUtil.redissonClient = redissonClient;
    }

    public static <T> void remove(String key) {
        RBucket<T> rBucket = redissonClient.getBucket(key);
        rBucket.delete();
    }

    public static <T> void remove(String key, String cacheKey) {
        RMapCache<String, T> rMapCache = redissonClient.getMapCache(key);
        rMapCache.remove(cacheKey);
    }

    public static <T> void put(String key, String cacheKey, T cacheValue) {
        RMapCache<String, T> rMapCache = redissonClient.getMapCache(key);
        rMapCache.put(cacheKey, cacheValue);
    }
}
