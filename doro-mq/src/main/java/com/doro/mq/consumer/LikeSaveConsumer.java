package com.doro.mq.consumer;

import com.doro.api.orm.UserLikeService;
import com.doro.bean.LikeIndexBean;
import com.doro.cache.utils.RedisUtil;
import com.doro.common.constant.Separator;
import com.doro.common.enumeration.LikeType;
import com.doro.common.enumeration.TopicEnum;
import com.doro.mq.base.BaseConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.redisson.api.RBucket;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

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
            for (MessageExt messageExt : msg) {
                String cacheKey = new String(messageExt.getBody(), StandardCharsets.UTF_8);
            }
            // 默认消费成功
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        };
    }

    private void execute(String cacheKey) {
        RBucket<Byte> bucket = RedisUtil.createBucket(cacheKey);
        Byte positive = bucket.get();
        String[] arr = cacheKey.split(Separator.COLON);
        long userId = Long.parseLong(arr[1]);
        short type = Short.parseShort(arr[2]);
        long objId = Long.parseLong(arr[3]);

        // TODO 获取每级对应的 id

        if (LikeType.POST.getType() == type) {
            LikeIndexBean first = new LikeIndexBean();
        } else if (LikeType.COMMENT.getType() == type) {
            LikeIndexBean first = new LikeIndexBean();
            LikeIndexBean second = new LikeIndexBean();
        } else if (LikeType.SUB_COMMENT.getType() == type) {
            LikeIndexBean first = new LikeIndexBean();
            LikeIndexBean second = new LikeIndexBean();
            LikeIndexBean third = new LikeIndexBean();
        }
    }
}
