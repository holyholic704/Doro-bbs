package com.doro.api.mq;

/**
 * @author jiage
 */
public interface UpdateCountMqService {

    void send(String cachePrefix, long id, Long count, long delayTime);
}
