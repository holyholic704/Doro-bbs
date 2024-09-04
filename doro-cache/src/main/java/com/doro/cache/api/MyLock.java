package com.doro.cache.api;

/**
 * 分布式锁操作
 * 不包括加锁操作，请使用 AcquireLock
 *
 * @author jiage
 */
public interface MyLock extends AutoCloseable {

    /**
     * 释放锁
     */
    void unlock();

    /**
     * 异步释放锁
     */
    void unlockAsync();

    /**
     * 强制释放锁，可以释放其他线程加的锁
     *
     * @return 如果锁存在且释放成功，返回 true，其他情况返回 false
     */
    boolean forceUnlock();

    /**
     * 是否已上锁（锁是否被某个线程持有）
     */
    boolean isLocked();

    /**
     * 锁是否被指定的线程持有
     *
     * @param threadId 线程 ID
     */
    boolean isHeldByThread(long threadId);

    /**
     * 锁是否被当前线程持有
     */
    boolean isHeldByCurrentThread();

    /**
     * 当前线程持有该锁的次数
     */
    int getHoldCount();

    /**
     * 锁的剩余存活时间
     *
     * @return -2 表示没有锁，-1 表示锁没有被指定释放时间
     */
    long remainTimeToLive();
}
