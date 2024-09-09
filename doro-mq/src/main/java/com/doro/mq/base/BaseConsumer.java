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
    private final TopicEnum topicEnum;

    protected BaseConsumer(TopicEnum topicEnum) {
        this.topicEnum = topicEnum;
    }

    protected abstract MessageListenerConcurrently registerListener();

    @Override
    public void start() throws MQClientException {
        consumer = new DefaultMQPushConsumer();
        consumer.setNamesrvAddr(nameServer);
        consumer.setConsumerGroup(topicEnum.getConsumerGroup());
        consumer.subscribe(topicEnum.getTopic(), "*");
        consumer.registerMessageListener(registerListener());
        consumer.start();
        log.info(this.topicEnum + "：启动");
    }
}
