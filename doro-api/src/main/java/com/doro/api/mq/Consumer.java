package com.doro.api.mq;

import com.doro.api.common.Runner;

/**
 * @author jiage
 */
public interface Consumer extends Runner {

    void start() throws Exception;

    @Override
    default void run() throws Exception {
        start();
    }
}
