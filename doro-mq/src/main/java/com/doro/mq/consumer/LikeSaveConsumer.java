package com.doro.mq.consumer;

import com.doro.api.orm.UserLikeService;
import com.doro.common.enumeration.TopicEnum;
import com.doro.mq.base.BaseConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.springframework.stereotype.Component;

/**
 * @author jiage
 */
@Component
@Slf4j
public class LikeSaveConsumer extends BaseConsumer {

    private final UserLikeService userLikeService;

    public LikeSaveConsumer(UserLikeService userLikeService) {
        this.userLikeService = userLikeService;
    }

    @Override
    protected TopicEnum getTopicEnum() {
        return TopicEnum.LIKE_SAVE;
    }

    @Override
    protected MessageListenerConcurrently registerListener() {
        return (msg, context) -> {
            // 默认消费成功
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        };
    }
}
