package com.doro.cache.utils;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.stereotype.Component;

@Component
public class CacheUtil {

    public static void t(Cache<?,?> cache) {
        cache.invalidateAll();
    }
}
