package com.doro.cache.utils;

import org.redisson.api.RBatch;
import org.redisson.api.RBucket;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jiage
 */
@Component
public class RedisUtil {

    private static RedissonClient redissonClient;

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        RedisUtil.redissonClient = redissonClient;
    }

    public static <T> RBucket<T> createBucket(String name) {
        return redissonClient.getBucket(name);
    }

    public static RBatch createBatch() {
        return redissonClient.createBatch();
    }

    public static <K, V> RMapCache<K, V> createMapCache(String name) {
        return redissonClient.getMapCache(name);
    }

}
