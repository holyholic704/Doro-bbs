package com.doro.mq.consumer;

import com.doro.api.common.Runner;
import com.doro.cache.utils.RedisUtil;
import com.doro.common.enumeration.TopicEnum;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author jiage
 */
@Component
public class UpdateCommentsConsumer implements Runner {

    @Value("${rocketmq.name-server}")
    private String nameServer;

    @Value("${rocketmq.consumer.access-key}")
    private String accessKey;

    @Value("${rocketmq.consumer.secret-key}")
    private String secretKey;

    private final DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(new AclClientRPCHook(new SessionCredentials(accessKey, secretKey)));

    @Override
    public void run() throws Exception {
        consumer.setNamesrvAddr(nameServer);
        consumer.setConsumerGroup(TopicEnum.UPDATE_COUNT.getConsumerGroup());
        consumer.subscribe(TopicEnum.UPDATE_COUNT.getTopic(), "*");
        consumer.registerMessageListener((MessageListenerConcurrently) (msg, context) -> {
            try {
                for (MessageExt messageExt : msg) {
                    String message = new String(messageExt.getBody());

                    RedisUtil.isExists(message);

                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 消费失败
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            // 默认消费成功
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
    }
}
