package com.doro.cache.utils;

import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    public static boolean isExists(String name) {
        return redissonClient.getBucket(name).isExists();
    }

    public static boolean delete(String name) {
        return redissonClient.getBucket(name).delete();
    }

    public static <T> RBucket<T> createBucket(String name) {
        return redissonClient.getBucket(name);
    }

    public static RedisBatch initBatch(String name) {
        return new RedisBatch(name);
    }

    public static RBatch createBatch() {
        // 保证原子性
        return redissonClient.createBatch(BatchOptions.defaults().executionMode(BatchOptions.ExecutionMode.IN_MEMORY_ATOMIC));
    }

    public static <K, V> RMap<K, V> createMap(String name) {
        return redissonClient.getMap(name, StringCodec.INSTANCE);
    }

    public static <K, V> RMapCache<K, V> createMapCache(String name) {
        return redissonClient.getMapCache(name, StringCodec.INSTANCE);
    }

    public static <T> RScoredSortedSet<T> createSortedset(String name) {
        return redissonClient.getScoredSortedSet(name);
    }

    public static RKeys getKeys() {
        return redissonClient.getKeys();
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

        public <K, V> RedisBatch mapPut(K k, V v) {
            batch.getMap(name, StringCodec.INSTANCE).putAsync(k, v);
            return this;
        }

        public <K, V> RedisBatch mapPutAll(Map<? extends K, ? extends V> map) {
            batch.getMap(name, StringCodec.INSTANCE).putAllAsync(map);
            return this;
        }

        public <K> RedisBatch mapGet(K k) {
            batch.getMap(name, StringCodec.INSTANCE).getAsync(k);
            return this;
        }

        public <K, V> RedisBatch mapReplaceCas(K k, V expect, V newValue) {
            batch.getMap(name, StringCodec.INSTANCE).replaceAsync(k, expect, newValue);
            return this;
        }

        public <K> RedisBatch mapAddAndGet(K k, Number delta) {
            batch.getMap(name, StringCodec.INSTANCE).addAndGetAsync(k, delta);
            return this;
        }

        public <K, V> RedisBatch mapPutIfAbsent(K k, V v) {
            batch.getMap(name, StringCodec.INSTANCE).putIfAbsentAsync(k, v);
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

        public RedisBatch setReadAll() {
            batch.getSet(name).readAllAsync();
            return this;
        }

        public List<?> execute() {
            return batch.execute().getResponses();
        }
    }
}
