package com.doro.mq.consumer;

import cn.hutool.core.util.ObjectUtil;
import com.doro.api.common.CountService;
import com.doro.cache.api.MyLock;
import com.doro.cache.utils.LockUtil;
import com.doro.cache.utils.RedisUtil;
import com.doro.common.constant.CacheKey;
import com.doro.common.constant.CommonConst;
import com.doro.common.enumeration.TopicEnum;
import com.doro.mq.base.BaseConsumer;
import com.doro.mq.dto.CountMqModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author jiage
 */
@Component
@Slf4j
public class UpdateCountConsumer extends BaseConsumer {

    private final Map<String, CountService> countServiceMap;

    @Autowired
    public UpdateCountConsumer(Map<String, CountService> countServiceMap) {
        super(TopicEnum.UPDATE_COUNT);
        this.countServiceMap = countServiceMap;
    }

    @Override
    protected MessageListenerConcurrently registerListener() {
        return (msg, context) -> {
            for (MessageExt messageExt : msg) {
                try {
                    CountMqModel countMqModel = ObjectUtil.deserialize(messageExt.getBody());
                    doUpdate(countMqModel);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info(e.getMessage());
                }
            }
            // 默认消费成功
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        };
    }

    private void doUpdate(CountMqModel countMqModel) {
        String cachePrefix = countMqModel.getCachePrefix();
        long id = countMqModel.getId();
        String cacheKey = cachePrefix + id;
        try (MyLock lock = LockUtil.lock(cacheKey, CommonConst.COMMON_LOCK_LEASE_SECONDS)) {
            delNotConsumedCache(cacheKey);
            CountService countService = countServiceMap.get(cachePrefix);
            if (countService != null) {
                countService.correctCount(id, countMqModel.getCount());
            }
        } catch (Exception e) {
            e.printStackTrace();
            delNotConsumedCache(cacheKey);
        }
    }

    private void delNotConsumedCache(String cacheKey) {
        RedisUtil.createBucket(CacheKey.COUNT_STILL_NOT_CONSUMED_PREFIX + cacheKey).delete();
    }
}
