package com.doro.cache.utils;

import com.doro.cache.api.AcquireLock;
import com.doro.cache.api.MyLock;
import com.doro.cache.constant.CacheConstant;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁工具包
 * TODO 读写锁引入？
 *
 * @author jiage
 */
@Component
public class LockUtil {

    private static RedissonClient redissonClient;

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        LockUtil.redissonClient = redissonClient;
    }

    /**
     * 加锁，注意及时释放
     *
     * @param key 键
     */
    public static MyLock lock(String key) {
        return LockUtil.lock(key, 0, TimeUnit.SECONDS, false);
    }

    /**
     * 加锁，必须传入释放时间，单位秒
     *
     * @param key       键
     * @param leaseTime 释放时间
     */
    public static MyLock lock(String key, long leaseTime) {
        Assert.isTrue(leaseTime > 0, "必须传入释放时间");
        return LockUtil.lock(key, leaseTime, TimeUnit.SECONDS, false);
    }

    /**
     * 加锁
     *
     * @param key       键
     * @param leaseTime 释放时间
     * @param unit      时间单位
     * @param fair      公平实现
     */
    public static MyLock lock(String key, long leaseTime, TimeUnit unit, boolean fair) {
        InnerLock rLock = LockUtil.getLock(key, fair);
        if (leaseTime > 0) {
            rLock.lock(leaseTime, unit);
        } else {
            rLock.lock();
        }
        return rLock;
    }

    /**
     * 加锁尝试
     *
     * @param key 键
     */
    public static MyLock tryLock(String key) {
        return LockUtil.tryLock(key, 0, 0, TimeUnit.SECONDS, false);
    }

    /**
     * 加锁尝试
     *
     * @param key       键
     * @param waitTime  加锁等待时间
     * @param leaseTime 释放时间
     */
    public static MyLock tryLock(String key, long waitTime, long leaseTime) {
        return LockUtil.tryLock(key, waitTime, leaseTime, TimeUnit.SECONDS, false);
    }

    /**
     * 加锁尝试
     *
     * @param key       键
     * @param waitTime  加锁等待时间
     * @param leaseTime 释放时间
     * @param unit      时间单位
     * @param fair      公平实现
     */
    public static MyLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean fair) {
        InnerLock lock = LockUtil.getLock(key, fair);
        boolean acquired;
        try {
            if (waitTime == 0 && leaseTime == 0) {
                acquired = lock.tryLock();
            } else {
                acquired = lock.tryLock(waitTime, leaseTime, unit);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return acquired ? lock : null;
    }

    /**
     * 释放锁
     */
    public static void unlock(String key) {
        LockUtil.unlock(LockUtil.getLock(key, false));
    }

    /**
     * 释放锁
     */
    public static void unlock(MyLock lock) {
        lock.unlock();
    }

    /**
     * 异步释放锁
     */
    public static void unlockAsync(String key) {
        LockUtil.unlockAsync(LockUtil.getLock(key, false));
    }

    /**
     * 异步释放锁
     */
    public static void unlockAsync(MyLock lock) {
        lock.unlockAsync();
    }

    /**
     * 是否已上锁（锁是否被某个线程持有）
     */
    public static boolean isLocked(String key) {
        return LockUtil.isLocked(LockUtil.getLock(key, false));
    }

    /**
     * 是否已上锁（锁是否被某个线程持有）
     */
    public static boolean isLocked(MyLock lock) {
        return lock.isLocked();
    }

    /**
     * 强制释放锁，可以释放其他线程加的锁
     *
     * @return 如果锁存在且释放成功，返回 true，其他情况返回 false
     */
    public static boolean forceUnlock(String key) {
        return LockUtil.forceUnlock(LockUtil.getLock(key, false));
    }

    /**
     * 强制释放锁，可以释放其他线程加的锁
     *
     * @return 如果锁存在且释放成功，返回 true，其他情况返回 false
     */
    public static boolean forceUnlock(MyLock lock) {
        return lock.forceUnlock();
    }

    /**
     * 锁是否被指定的线程持有
     */
    public static boolean isHeldByThread(String key, long threadId) {
        return LockUtil.isHeldByThread(LockUtil.getLock(key, false), threadId);
    }

    /**
     * 锁是否被指定的线程持有
     */
    public static boolean isHeldByThread(MyLock lock, long threadId) {
        return lock.isHeldByThread(threadId);
    }

    /**
     * 锁是否被当前线程持有
     */
    public static boolean isHeldByCurrentThread(String key) {
        return LockUtil.isHeldByCurrentThread(LockUtil.getLock(key, false));
    }

    /**
     * 锁是否被当前线程持有
     */
    public static boolean isHeldByCurrentThread(MyLock lock) {
        return lock.isHeldByCurrentThread();
    }

    /**
     * 当前线程持有该锁的次数
     */
    public static int getHoldCount(String key) {
        return LockUtil.getHoldCount(LockUtil.getLock(key, false));
    }

    /**
     * 当前线程持有该锁的次数
     */
    public static int getHoldCount(MyLock lock) {
        return lock.getHoldCount();
    }

    /**
     * 锁的剩余存活时间
     *
     * @return -2 表示没有锁，-1 表示锁没有被指定释放时间
     */
    public static long remainTimeToLive(String key) {
        return LockUtil.remainTimeToLive(LockUtil.getLock(key, false));
    }

    /**
     * 锁的剩余存活时间
     *
     * @return -2 表示没有锁，-1 表示锁没有被指定释放时间
     */
    public static long remainTimeToLive(MyLock lock) {
        return lock.remainTimeToLive();
    }

    /**
     * 获取锁对象
     * 注意，公平实现、非公平实现为两种不同的锁对象
     *
     * @param key  键
     * @param fair 公平实现
     * @return 锁对象
     */
    private static InnerLock getLock(String key, boolean fair) {
        return new InnerLock(fair ? redissonClient.getFairLock(CacheConstant.LOCK_FAIR_PREFIX + key)
                : redissonClient.getLock(CacheConstant.LOCK_PREFIX + key));
    }

    private final static class InnerLock implements MyLock, AcquireLock {

        private final RLock rLock;

        private InnerLock(RLock rLock) {
            this.rLock = rLock;
        }

        @Override
        public void lock() {
            this.rLock.lock();
        }

        @Override
        public void lock(long leaseTime, TimeUnit unit) {
            this.rLock.lock(leaseTime, unit);
        }

        @Override
        public boolean tryLock() throws InterruptedException {
            return this.rLock.tryLock();
        }

        @Override
        public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
            return this.rLock.tryLock(waitTime, leaseTime, unit);
        }

        @Override
        public void unlock() {
            this.rLock.unlock();
        }

        @Override
        public void unlockAsync() {
            this.rLock.unlockAsync();
        }

        @Override
        public boolean forceUnlock() {
            return this.rLock.forceUnlock();
        }

        @Override
        public boolean isLocked() {
            return this.rLock.isLocked();
        }

        @Override
        public boolean isHeldByThread(long threadId) {
            return this.rLock.isHeldByThread(threadId);
        }

        @Override
        public boolean isHeldByCurrentThread() {
            return this.rLock.isHeldByCurrentThread();
        }

        @Override
        public int getHoldCount() {
            return this.rLock.getHoldCount();
        }

        @Override
        public long remainTimeToLive() {
            return this.rLock.remainTimeToLive();
        }
    }
}
