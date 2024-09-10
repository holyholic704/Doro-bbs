package com.doro.mq.base;

import com.doro.api.mq.Consumer;
import com.doro.common.enumeration.TopicEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author jiage
 */
@Slf4j
public abstract class BaseConsumer implements Consumer {

    @Value("${rocketmq.name-server}")
    private String nameServer;

    private DefaultMQPushConsumer consumer;

    protected abstract TopicEnum getTopicEnum();

    protected abstract MessageListenerConcurrently registerListener();

    @Override
    public void start() throws MQClientException {
        consumer = new DefaultMQPushConsumer();
        consumer.setNamesrvAddr(nameServer);
        TopicEnum topicEnum = getTopicEnum();
        consumer.setConsumerGroup(topicEnum.getConsumerGroup());
        consumer.subscribe(topicEnum.getTopic(), "*");
        consumer.registerMessageListener(registerListener());
        consumer.start();
        log.info(topicEnum + "：消费者启动");
    }
}
