package com.doro.cache.utils;

import org.redisson.api.BatchResult;
import org.redisson.api.RBatch;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

/**
 * @author jiage
 */
@Component
public class RemoteCacheUtil {

    private static RedissonClient redissonClient;

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        RemoteCacheUtil.redissonClient = redissonClient;
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        return (T) getBucket(key).get();
    }

    public static void put(String key, Object value) {
        getBucket(key).set(value);
    }

    public static void put(String key, Object value, Duration duration) {
        getBucket(key).set(value, duration);
    }

    public static <T> T computeIfAbsent(String key, Function<String, T> function) {
        return computeIfAbsent(key, function, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T computeIfAbsent(String key, Function<String, T> function, Duration duration) {
        T value = function.apply(key);

        RBatch rBatch = createBatch();
        rBatch.getBucket(key).getAsync();
        if (duration != null) {
            rBatch.getBucket(key).setIfAbsentAsync(value, duration);
        } else {
            rBatch.getBucket(key).setIfAbsentAsync(value);
        }
        rBatch.execute();

        BatchResult<?> batchResult = rBatch.execute();
        List<?> responses = batchResult.getResponses();

        Boolean isSuccess = (Boolean) responses.get(1);
        return isSuccess != null && isSuccess ? value : (T) responses.get(0);
    }

    public static void remove(String key) {
        getBucket(key).delete();
    }

    private static RBucket<Object> getBucket(String name) {
        return redissonClient.getBucket(name);
    }

    private static RBatch createBatch() {
        return redissonClient.createBatch();
    }
}
