package com.doro.cache.processor;

import com.doro.cache.listener.DelMessageListener;
import com.doro.common.constant.CacheConstant;
import com.doro.common.constant.Separator;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author jiage
 */
@Component
public class DelMessageProcessor {

    @Value("${worker-id}")
    private short workerId;

    private final RTopic delTopic;

    @Autowired
    public DelMessageProcessor(RedissonClient redissonClient) {
        delTopic = redissonClient.getTopic(CacheConstant.DEL_LOCAL_CACHE_TOPIC);
        delTopic.addListener(String.class, new DelMessageListener());
    }

    public void send(String key) {
        delTopic.publishAsync(workerId + Separator.UNDERSCORE + key);
    }
}
