package com.doro.api.mq;

import com.doro.api.common.Runner;

import java.nio.charset.StandardCharsets;

/**
 * @author jiage
 */
public interface Producer extends Runner {

    void start() throws Exception;

    @Override
    default void run() throws Exception {
        start();
    }

    default boolean send(String message) {
        return send(message, 0);
    }

    default boolean send(String message, long delayTime) {
        return send(message.getBytes(StandardCharsets.UTF_8), delayTime);
    }

    default boolean send(byte[] message) {
        return send(message, 0);
    }

    boolean send(byte[] message, long delayTime);
}