package com.doro.cache.utils;

import com.doro.cache.properties.LocalCacheProperties;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@DependsOn("lockUtil")
public class CacheUtil {

    private static final LocalCacheProperties NORMAL_LOCAL_CACHE_PROPERTIES = new LocalCacheProperties();
    private static final Cache<String, Cache<String, Object>> LOCAL_CACHE_MAP = initLocalCache(NORMAL_LOCAL_CACHE_PROPERTIES);

    public static void addLocalCache(String key, String cacheKey, Object cacheValue) {
        addLocalCache(key, cacheKey, cacheValue, NORMAL_LOCAL_CACHE_PROPERTIES);
    }

    public static void addLocalCache(String key, String cacheKey, Object cacheValue, LocalCacheProperties localCacheProperties) {
        Cache<String, Object> localCache = LOCAL_CACHE_MAP.getIfPresent(key);
        if (localCache != null) {
            localCache.put(cacheKey, cacheValue);
        } else {
            synchronized (key.intern()) {
                // 二次检查
                localCache = LOCAL_CACHE_MAP.getIfPresent(key);
                if (localCache == null) {
                    LOCAL_CACHE_MAP.put(key, localCache = initLocalCache(localCacheProperties));
                }
                localCache.put(cacheKey, cacheValue);
            }
        }
    }

    /**
     * 建议与添加缓存一起使用，应避免单独使用
     *
     * @param key
     * @param cacheKey
     */
    public static void removeLocalCache(String key, String cacheKey) {
        Cache<String, Object> localCache = LOCAL_CACHE_MAP.getIfPresent(key);
        if (localCache != null) {
            synchronized (key.intern()) {
                // 二次检查
                localCache = LOCAL_CACHE_MAP.getIfPresent(key);
                if (localCache != null) {
                    localCache.invalidate(cacheKey);
                    if (localCache.estimatedSize() == 0) {
//                        LOCAL_CACHE_MAP.invalidate(key);
//                        LOCAL_LOCK_MAP.remove(CacheConstant.LOCAL_LOCK_PREFIX + key);
                    }
                }
            }
        }
    }

    private static <T> Cache<String, T> initLocalCache(LocalCacheProperties localCacheProperties) {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
        if (localCacheProperties.getExpireTime() != null) {
            if (localCacheProperties.isExpireAfterAccess()) {
                caffeine.expireAfterAccess(localCacheProperties.getExpireTime(), localCacheProperties.getUnit());
            } else {
                caffeine.expireAfterWrite(localCacheProperties.getExpireTime(), localCacheProperties.getUnit());
            }
        }
        if (localCacheProperties.getMaxSize() != null) {
            caffeine.maximumSize(localCacheProperties.getMaxSize());
        }
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
        CountDownLatch latch = new CountDownLatch(2);
        new Thread(() -> {
            addLocalCache("FUCK", "shit", "good");
            latch.countDown();
        }).start();

        Thread.sleep(500);

        new Thread(() -> {
            removeLocalCache("FUCK", "shit");
            latch.countDown();
        }).start();

        latch.await();

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
