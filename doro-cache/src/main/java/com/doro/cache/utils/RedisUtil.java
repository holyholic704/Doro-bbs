package com.doro.cache.utils;

import org.redisson.api.*;
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

    public static <T> RScoredSortedSet<T> createSortedset(String name) {
        return redissonClient.getScoredSortedSet(name);
    }

    public static <T> RList<T> createList(String name) {
        return redissonClient.getList(name);
    }

    public static RAtomicLong createAtomicLong(String name) {
        return redissonClient.getAtomicLong(name);
    }
}
