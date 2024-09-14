package com.doro.mq.producer;

import com.doro.common.enumeration.TopicEnum;
import com.doro.mq.base.BaseProducer;
import org.springframework.stereotype.Component;

/**
 * @author jiage
 */
@Component
public class LikeSaveProducer extends BaseProducer {

    @Override
    protected TopicEnum getTopicEnum() {
        return TopicEnum.LIKE_SAVE;
    }

}
