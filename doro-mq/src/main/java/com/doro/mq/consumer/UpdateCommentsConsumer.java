package com.doro.mq.consumer;

import cn.hutool.json.JSONUtil;
import com.doro.api.common.CountService;
import com.doro.api.common.Runner;
import com.doro.mq.dto.CountMqModel;
import com.doro.cache.api.MyLock;
import com.doro.cache.utils.LockUtil;
import com.doro.cache.utils.RedisUtil;
import com.doro.common.constant.CacheKey;
import com.doro.common.constant.CommonConst;
import com.doro.common.enumeration.TopicEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author jiage
 */
@Component
@Slf4j
public class UpdateCommentsConsumer implements Runner {

    @Value("${rocketmq.name-server}")
    private String nameServer;

    private final Map<String, CountService> countServiceMap;

    @Autowired
    public UpdateCommentsConsumer(Map<String, CountService> countServiceMap) {
        this.countServiceMap = countServiceMap;
    }

    private final DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();

    @Override
    public void run() throws Exception {
        consumer.setNamesrvAddr(nameServer);
        consumer.setConsumerGroup(TopicEnum.UPDATE_COUNT.getConsumerGroup());
        consumer.subscribe(TopicEnum.UPDATE_COUNT.getTopic(), "*");
        consumer.registerMessageListener((MessageListenerConcurrently) (msg, context) -> {
            for (MessageExt messageExt : msg) {
                String message = new String(messageExt.getBody());
                CountMqModel countMqModel = JSONUtil.toBean(message, CountMqModel.class);
                doUpdate(countMqModel);
            }
            // 默认消费成功
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        log.info("消费者启动");
    }

    private void doUpdate(CountMqModel countMqModel) {
        String cachePrefix = countMqModel.getCachePrefix();
        long id = countMqModel.getId();
        String cacheKey = cachePrefix + id;
        try (MyLock lock = LockUtil.lock(cacheKey, CommonConst.COMMON_LOCK_LEASE_SECONDS)) {
            RedisUtil.createBucket(CacheKey.COUNT_STILL_NOT_CONSUMED_PREFIX + cacheKey).delete();
            CountService countService = countServiceMap.get(cachePrefix);
            if (countService != null) {
                countService.correctCount(id, countMqModel.getCount());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
