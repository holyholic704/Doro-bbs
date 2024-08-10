package com.doro.cache.utils;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class LockUtil {

    private static RedissonClient redissonClient;

    @Autowired
    public static void setRedissonClient(RedissonClient redissonClient) {
        LockUtil.redissonClient = redissonClient;
    }

    public static RLock lock(String key) {
        return lock(key, 0, TimeUnit.SECONDS, false);
    }

    public static RLock lock(String key, long leaseTime) {
        return lock(key, leaseTime, TimeUnit.SECONDS, false);
    }

    public static RLock lock(String key, long leaseTime, TimeUnit unit, boolean fair) {
        RLock rLock = getLock(key, fair);
        if (leaseTime > 0) {
            rLock.lock(leaseTime, unit);
        } else {
            rLock.lock();
        }
        return rLock;
    }

    public static RLock tryLock(String key, long waitTime) {
        return tryLock(key, waitTime, 0, TimeUnit.SECONDS, false);
    }

    public static RLock tryLock(String key, long waitTime, long leaseTime) {
        return tryLock(key, waitTime, leaseTime, TimeUnit.SECONDS, false);
    }

    public static RLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean fair) {
        RLock rLock = getLock(key, fair);
        boolean acquired;
        try {
            if (leaseTime > 0) {
                acquired = rLock.tryLock(waitTime, leaseTime, unit);
            } else {
                acquired = rLock.tryLock();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return acquired ? rLock : null;
    }

    public static void unLock(RLock lock) {
        if (lock.isLocked()) {
            lock.unlock();
        }
    }

    public static void unLock(String key) {
        RLock rLock = getLock(key, false);
        rLock.unlock();
    }

    public static void unLock(String key, boolean fair) {
        RLock rLock = getLock(key, fair);
        rLock.unlock();
    }

    private static RLock getLock(String key, boolean fair) {
        return fair ? redissonClient.getFairLock(key)
                : redissonClient.getLock(key);
    }

    abstract class MyLock implements RLock {
    }
}
