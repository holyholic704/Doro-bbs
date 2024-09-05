package com.doro.mq.consumer;

import com.doro.api.common.Runner;
import com.doro.api.mq.UpdateCountMqService;
import com.doro.common.enumeration.TopicEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author jiage
 */
@Component
@Slf4j
public class UpdateCommentsConsumer implements Runner {

    @Value("${rocketmq.name-server}")
    private String nameServer;

    private final UpdateCountMqService updateCountMqService;

    @Autowired
    public UpdateCommentsConsumer(UpdateCountMqService updateCountMqService) {
        this.updateCountMqService = updateCountMqService;
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
                updateCountMqService.updateCount(message);
            }
            // 默认消费成功
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        log.info("消费者启动");
    }
}
