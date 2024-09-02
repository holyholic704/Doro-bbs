package com.doro.common.api;

/**
 * 实现该接口的类在应用启动时会自动运行 run 方法
 *
 * @author jiage
 */
public interface Runner {

    /**
     * 待运行的逻辑
     */
    void run() throws Exception;
}
