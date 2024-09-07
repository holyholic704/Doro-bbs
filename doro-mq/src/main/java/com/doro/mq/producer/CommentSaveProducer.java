package com.doro.mq.producer;

import com.doro.api.common.Runner;
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
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author jiage
 */
@Component
@Slf4j
public class CommentSaveProducer implements Runner {

    @Value("${rocketmq.name-server}")
    private String nameServer;

    @Value("${rocketmq.producer.group}")
    private String producerGroup;

    private final DefaultMQProducer producer = new DefaultMQProducer();

    @Override
    public void run() throws MQClientException {
        producer.setProducerGroup(producerGroup);
        producer.setNamesrvAddr(nameServer);
        producer.start();
        log.info("save生产者启动");
    }

    public boolean send(String message, long delayTime) {
        return send(message.getBytes(StandardCharsets.UTF_8), delayTime);
    }

    public boolean send(byte[] message, long delayTime) {
        Message msg = new Message(TopicEnum.BATCH_SAVE.getTopic(), message);
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