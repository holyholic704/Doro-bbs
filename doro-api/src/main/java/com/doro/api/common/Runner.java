package com.doro.api.common;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * 对需要自启动的类进行统一管理
 * 实现该接口的类在应用启动时会自动运行 run 方法
 *
 * @author jiage
 */
public interface Runner extends ApplicationRunner {

    /**
     * 待运行的逻辑
     */
    void run() throws Exception;

    @Override
    default void run(ApplicationArguments args) throws Exception {
        run();
    }
}
