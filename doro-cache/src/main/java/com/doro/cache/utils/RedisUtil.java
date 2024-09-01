package com.doro.cache.utils;

import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

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

    public static RedisBatch initBatch(String name) {
        return new RedisBatch(name);
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

    public static <T> RBlockingQueue<T> createBlockingQueue(String name) {
        return redissonClient.getBlockingQueue(name);
    }

    public static <T> RSet<T> createSet(String name) {
        return redissonClient.getSet(name);
    }

    public static class RedisBatch {

        private final RBatch batch = RedisUtil.createBatch();
        private final String name;

        private RedisBatch(String name) {
            this.name = name;
        }

        public RedisBatch expire(Duration duration) {
            batch.getBucket(name).expireAsync(duration);
            return this;
        }

        public RedisBatch isExists() {
            batch.getBucket(name).isExistsAsync();
            return this;
        }

        public RedisBatch delete() {
            batch.getBucket(name).deleteAsync();
            return this;
        }

        public RedisBatch atomicLongCas(long expect, long update) {
            batch.getAtomicLong(name).compareAndSetAsync(expect, update);
            return this;
        }

        public RedisBatch atomicLongSet(long value) {
            batch.getAtomicLong(name).setAsync(value);
            return this;
        }

        public RedisBatch listSize() {
            batch.getList(name).sizeAsync();
            return this;
        }

        public RedisBatch listGet(int index) {
            batch.getList(name).getAsync(index);
            return this;
        }

        public <T> RedisBatch listAddAll(Collection<? extends T> c) {
            batch.getList(name).addAllAsync(c);
            return this;
        }

        public List<?> execute() {
            return batch.execute().getResponses();
        }
    }
}
