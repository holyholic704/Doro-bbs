package com.doro.common.api;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
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
     * 加锁尝试，建议使用带有超时时间的版本
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
