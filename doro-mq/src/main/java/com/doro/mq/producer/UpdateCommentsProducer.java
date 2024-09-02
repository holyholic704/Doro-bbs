package com.doro.mq.producer;

import com.doro.common.api.Runner;
import com.doro.common.constant.TopicConst;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author jiage
 */
@Component
public class UpdateCommentsProducer implements Runner {

    @Value("${rocketmq.name-server}")
    private String nameServer;

    @Value("${rocketmq.producer.group}")
    private String producerGroup;

    @Value("${rocketmq.access-key}")
    private String accessKey;

    @Value("${rocketmq.secret-key}")
    private String secretKey;

    private final DefaultMQProducer producer = new DefaultMQProducer(new AclClientRPCHook(new SessionCredentials(accessKey, secretKey)));

    @Override
    public void run() throws MQClientException {
        producer.setProducerGroup(producerGroup);
        producer.setNamesrvAddr(nameServer);
        producer.start();
        System.out.println("启动");
    }

    public void send(String message) {
        send(message, 0);
    }

    public void send(String message, long delayTime) {
        Message msg = new Message(TopicConst.UPDATE_COMMENTS, message.getBytes());
        if (delayTime > 0) {
            msg.setDelayTimeMs(delayTime * 1000);
        }
        SendResult sendResult;
        try {
            sendResult = producer.send(msg);
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(sendResult);
    }
}
