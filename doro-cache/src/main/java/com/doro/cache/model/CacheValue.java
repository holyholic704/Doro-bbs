package com.doro.cache.model;

/**
 * 缓存对象
 *
 * @author jiage
 */

public class CacheValue {

    private Object value;

    private long version = System.currentTimeMillis();

    private boolean sync;

    private CacheValue() {
    }

    public Object get() {
        return value;
    }

    public static CacheValue build(Object value) {
        return new CacheValue();
    }
}
