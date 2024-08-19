package com.doro.cache.flush;

import com.doro.cache.constant.CacheConstant;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息处理，刷新本地缓存
 *
 * @author jiage
 */
@Component
public class FlushLocalCacheProcessor {

    private static RedissonClient redissonClient;

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        FlushLocalCacheProcessor.redissonClient = redissonClient;
        addListener();
    }

    public static void send(String key) {
        redissonClient.getTopic(CacheConstant.FLUSH_LOCAL_CACHE_TOPIC)
                .publish(key);
    }

    private void addListener() {
        redissonClient.getTopic(CacheConstant.FLUSH_LOCAL_CACHE_TOPIC)
                .addListener(String.class, new FlushLocalCacheListener());
    }
}
