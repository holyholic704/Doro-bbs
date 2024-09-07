package com.doro.mq.producer;

import cn.hutool.json.JSONUtil;
import com.doro.api.common.Runner;
import com.doro.api.mq.UpdateCountMqService;
import com.doro.common.enumeration.TopicEnum;
import com.doro.mq.dto.CountMqModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
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
public class UpdateCommentsProducer implements Runner, UpdateCountMqService {

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
        log.info("生产者启动");
    }

    @Override
    public void send(String cachePrefix, long id, Long count, long delayTime) {
        String message = JSONUtil.toJsonStr(new CountMqModel(cachePrefix, id, count));
        Message msg = new Message(TopicEnum.UPDATE_COUNT.getTopic(), message.getBytes(StandardCharsets.UTF_8));
        if (delayTime > 0) {
            msg.setDelayTimeSec(delayTime);
        }
        try {
            SendResult sendResult = producer.send(msg);
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
