package com.doro.cache.utils;

import com.doro.cache.properties.LocalCacheProperties;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.DependsOn;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@DependsOn("lockUtil")
public class CacheUtil {

    /**
     * 本地缓存集合
     */
    private static final Cache<String, Cache<String, ?>> LOCAL_CACHE_MAP = initLocalCache(LocalCacheProperties.NORMAL);

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> T getLocal(String key, String cacheKey) {
        Cache<String, Object> localCache = (Cache<String, Object>) LOCAL_CACHE_MAP.getIfPresent(key);
        if (localCache != null) {
            return (T) localCache.getIfPresent(cacheKey);
        }
        return null;
    }

    public static <T> T computeLocalIfAbsent(String key, String cacheKey, T cacheValue) {
        return computeLocalIfAbsent(key, cacheKey, cacheValue, LocalCacheProperties.NORMAL);
    }

    @SuppressWarnings("unchecked")
    public static <T> T computeLocalIfAbsent(String key, String cacheKey, T cacheValue, LocalCacheProperties localCacheProperties) {
        Cache<String, Object> localCache = (Cache<String, Object>) LOCAL_CACHE_MAP.getIfPresent(key);
        if (localCache != null) {
            T value = (T) localCache.getIfPresent(cacheKey);
            if (value != null) {
                return value;
            }
            localCache.put(cacheKey, cacheValue);
        } else {
            putLocal(key, cacheKey, cacheValue, localCacheProperties);
        }
        return cacheValue;
    }

    public static <T> void putLocal(String key, String cacheKey, Object cacheValue) {
        putLocal(key, cacheKey, cacheValue, LocalCacheProperties.NORMAL);
    }

    @SuppressWarnings("unchecked")
    public static <T> void putLocal(String key, String cacheKey, T cacheValue, LocalCacheProperties localCacheProperties) {
        Cache<String, T> localCache = (Cache<String, T>) LOCAL_CACHE_MAP.getIfPresent(key);
        if (localCache != null) {
            localCache.put(cacheKey, cacheValue);
        } else {
            synchronized (key.intern()) {
                // 二次检查
                localCache = (Cache<String, T>) LOCAL_CACHE_MAP.getIfPresent(key);
                if (localCache == null) {
                    LOCAL_CACHE_MAP.put(key, localCache = initLocalCache(localCacheProperties));
                }
                localCache.put(cacheKey, cacheValue);
            }
        }
    }

    /**
     * 建议与添加缓存一起使用，应避免单独使用
     */
    @SuppressWarnings("unchecked")
    public static <T> void removeLocal(String key, String cacheKey) {
        Cache<String, Object> localCache = (Cache<String, Object>) LOCAL_CACHE_MAP.getIfPresent(key);
        if (localCache != null) {
            synchronized (key.intern()) {
                // 二次检查
                localCache = (Cache<String, Object>) LOCAL_CACHE_MAP.getIfPresent(key);
                if (localCache != null) {
                    localCache.invalidate(cacheKey);
                    if (localCache.estimatedSize() == 0) {
                        LOCAL_CACHE_MAP.invalidate(key);
                    }
                }
            }
        }
    }

    /**
     * 初始化一个本地缓存
     *
     * @param localCacheProperties 本地缓存配置
     * @return 本地缓存
     */
    private static <T> Cache<String, T> initLocalCache(LocalCacheProperties localCacheProperties) {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder();

        // 设置超时时间
        if (localCacheProperties.getExpireTime() != null) {
            if (localCacheProperties.isExpireAfterAccess()) {
                caffeine.expireAfterAccess(localCacheProperties.getExpireTime(), localCacheProperties.getUnit());
            } else {
                caffeine.expireAfterWrite(localCacheProperties.getExpireTime(), localCacheProperties.getUnit());
            }
        }

        // 设置最大容量
        if (localCacheProperties.getMaxSize() != null) {
            caffeine.maximumSize(localCacheProperties.getMaxSize());
        }

        // 设置值的引用类型
        if (localCacheProperties.getReferenceType() != null) {
            switch (localCacheProperties.getReferenceType()) {
                case SOFT:
                    caffeine.softValues();
                    break;
                case WEAK:
                    caffeine.weakValues();
                    break;
            }
        }

        return caffeine.build();
    }

    public static void main(String[] args) throws InterruptedException {
//
//        CountDownLatch latch = new CountDownLatch(10);
//        long l1 = System.currentTimeMillis();
//        ThreadGroup group = new ThreadGroup("fuck");
//        for (int i = 0; i < 1000; i++) {
//            new Thread(group, () -> {
//                for (int j = 0; j < 100; j++) {
//                    addLocalCache(Thread.currentThread().getName(), String.valueOf(j), new Test());
//                }
//                latch.countDown();
//            }, String.valueOf(i)).start();
//        }
////
//        latch.await();
//        long l2 = System.currentTimeMillis();
//
//        CountDownLatch latch2 = new CountDownLatch(1000);
//        for (int i = 0; i < 1000; i++) {
//            new Thread(group, () -> {
//                for (int j = 0; j < 100; j++) {
//                    removeLocalCache(Thread.currentThread().getName(), String.valueOf(j));
//                }
//                latch2.countDown();
//            }, String.valueOf(i)).start();
//        }
//
//        latch2.await();
//        System.out.println(l2 - l1);
//        System.out.println(System.currentTimeMillis() - l2);
//
//        System.gc();
//        Thread.sleep(500);
//
//        System.out.println("done");
    }
}
