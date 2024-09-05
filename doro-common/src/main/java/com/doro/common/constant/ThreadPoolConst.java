package com.doro.common.constant;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author jiage
 */
public class ThreadPoolConst {

    public static final int IO_BOUND_MAX_POOL_SIZE = (int) (Runtime.getRuntime().availableProcessors() / (1 - 0.95));

    public static final int IO_BOUND_CORE_POOL_SIZE = IO_BOUND_MAX_POOL_SIZE / 2;

    public static final int IO_BOUND_QUEUE_CAPACITY = IO_BOUND_CORE_POOL_SIZE * 100;

    public static final int KEEP_ALIVE_SECONDS = 60;

    public static final RejectedExecutionHandler REJECTED_EXECUTION_HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();
}
