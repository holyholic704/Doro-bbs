package com.doro.api.mq;

import com.doro.api.common.Runner;

/**
 * @author jiage
 */
public interface Producer extends Runner {

    void start() throws Exception;

    @Override
    default void run() throws Exception {
        start();
    }

    default boolean send(Object message) {
        return send(message, 0);
    }

    boolean send(Object message, long delayTime);
}