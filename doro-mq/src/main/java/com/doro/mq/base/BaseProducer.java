package com.doro.mq.base;

import com.doro.api.mq.Producer;
import com.doro.common.enumeration.TopicEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author jiage
 */
@Slf4j
public abstract class BaseProducer implements Producer {

    @Value("${rocketmq.name-server}")
    private String nameServer;

    @Value("${rocketmq.producer.group}")
    private String producerGroup;

    private DefaultMQProducer producer;
    private final TopicEnum topicEnum;

    public BaseProducer(TopicEnum topicEnum) {
        this.topicEnum = topicEnum;
    }

    @Override
    public void start() throws MQClientException {
        producer = new DefaultMQProducer();
        producer.setProducerGroup(producerGroup);
        producer.setNamesrvAddr(nameServer);
        producer.start();
        log.info(this.topicEnum + "：生产者启动");
    }

    @Override
    public boolean send(byte[] message, long delayTime) {
        Message msg = new Message(topicEnum.getTopic(), message);
        if (delayTime > 0) {
            msg.setDelayTimeSec(delayTime);
        }

        SendResult sendResult;

        try {
            sendResult = producer.send(msg);
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return sendResult != null && SendStatus.SEND_OK.equals(sendResult.getSendStatus());
    }
}
