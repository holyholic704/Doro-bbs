package com.doro.core.service;

import com.doro.cache.api.MyLock;
import com.doro.cache.utils.LockUtil;

import java.util.function.Supplier;

/**
 * @author jiage
 */
public abstract class LockTest {

    public <T> void test(String key, long time, Supplier<T> supplier) {
        MyLock lock = LockUtil.lock(key, time);
        if (!isExist()) {
            supplier.get();
        }
        LockUtil.unlock(lock);
    }

    abstract boolean isExist();

}
