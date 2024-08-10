package com.doro.cache.utils;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Component
public class LockUtil {

    private final RedissonClient redissonClient;

    @Autowired
    public LockUtil(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    private void te() {
//        redissonClient.
    }

//    public

    private RLock getLock(String key, boolean fair) {
        return fair ? redissonClient.getFairLock(key)
                : redissonClient.getLock(key);
    }

    static final class MyLock implements Lock {


        @Override
        public void lock() {

        }

        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public void unlock() {

        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }
}
