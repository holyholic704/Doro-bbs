package com.doro.mq.consumer;

import cn.hutool.json.JSONUtil;
import com.doro.api.orm.CommentService;
import com.doro.bean.CommentBean;
import com.doro.common.enumeration.TopicEnum;
import com.doro.mq.base.BaseConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jiage
 */
@Component
@Slf4j
public class CommentSaveConsumer extends BaseConsumer {

    private final CommentService commentService;

    @Autowired
    public CommentSaveConsumer(CommentService commentService) {
        super(TopicEnum.BATCH_SAVE);
        this.commentService = commentService;
    }

    @Override
    protected MessageListenerConcurrently registerListener() {
        return (msg, context) -> {
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
        };
    }
}
