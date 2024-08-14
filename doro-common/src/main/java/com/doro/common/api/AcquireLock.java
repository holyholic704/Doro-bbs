package com.doro.common.api;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁的获取
 * 不包括除加锁以外的操作，请使用 MyLock
 */
public interface AcquireLock {

    /**
     * 加锁，注意及时释放
     */
    void lock();

    /**
     * 加锁
     *
     * @param leaseTime 释放时间
     * @param unit      时间单位
     */
    void lock(long leaseTime, TimeUnit unit);

    /**
     * 加锁尝试，立即返回结果
     *
     * @return 是否成功获取锁
     */
    boolean tryLock() throws InterruptedException;

    /**
     * 加锁尝试
     *
     * @param waitTime  加锁等待时间
     * @param leaseTime 释放时间
     * @param unit      时间单位
     * @return 是否成功获取锁
     */
    boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;
}
