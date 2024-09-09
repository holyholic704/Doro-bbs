package com.doro.mq.producer;

import cn.hutool.json.JSONUtil;
import com.doro.api.mq.UpdateCountMqService;
import com.doro.common.enumeration.TopicEnum;
import com.doro.mq.base.BaseProducer;
import com.doro.mq.dto.CountMqModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author jiage
 */
@Component
@Slf4j
public class UpdateCountProducer extends BaseProducer implements UpdateCountMqService {

    public UpdateCountProducer() {
        super(TopicEnum.UPDATE_COUNT);
    }

    @Override
    public void send(String cachePrefix, long id, Long count, long delayTime) {
        send(JSONUtil.toJsonStr(new CountMqModel(cachePrefix, id, count)), delayTime);
    }
}
