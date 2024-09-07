package com.doro.mq.consumer;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.doro.api.common.Runner;
import com.doro.api.orm.CommentService;
import com.doro.bean.CommentBean;
import com.doro.common.enumeration.TopicEnum;
import com.esotericsoftware.kryo.kryo5.Kryo;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author jiage
 */
@Component
@Slf4j
public class CommentSaveConsumer implements Runner {

    @Value("${rocketmq.name-server}")
    private String nameServer;

    private final DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();

    private final CommentService commentService;

    @Autowired
    public CommentSaveConsumer(CommentService commentService) {
        this.commentService = commentService;
    }

    @Override
    public void run() throws Exception {
        consumer.setNamesrvAddr(nameServer);
        consumer.setConsumerGroup(TopicEnum.BATCH_SAVE.getConsumerGroup());
        consumer.subscribe(TopicEnum.BATCH_SAVE.getTopic(), "*");
        consumer.registerMessageListener((MessageListenerConcurrently) (msg, context) -> {
            for (MessageExt messageExt : msg) {
                try {
                    CommentBean commentBean = JSONUtil.toBean(new String(messageExt.getBody()), CommentBean.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info(e.getMessage());
                }
            }
            // 默认消费成功
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        log.info("save消费者启动");
    }

}
