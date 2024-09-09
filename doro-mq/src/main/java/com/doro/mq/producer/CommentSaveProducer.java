package com.doro.mq.producer;

import com.doro.common.enumeration.TopicEnum;
import com.doro.mq.base.BaseProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author jiage
 */
@Component
@Slf4j
public class CommentSaveProducer extends BaseProducer {

    public CommentSaveProducer() {
        super(TopicEnum.BATCH_SAVE);
    }
}