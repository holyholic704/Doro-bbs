package com.doro.core.config;

import com.doro.common.constant.Separator;
import com.doro.common.constant.ThreadPoolConst;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author jiage
 */
@EnableAsync
@Configuration
public class ThreadPoolConfig {

    public static final String CORE_TASK = "coreTask";

    @Bean(name = CORE_TASK)
    public ThreadPoolTaskExecutor coreTask() {
        return create(ThreadPoolConst.IO_BOUND_CORE_POOL_SIZE,
                ThreadPoolConst.IO_BOUND_MAX_POOL_SIZE,
                ThreadPoolConst.KEEP_ALIVE_SECONDS,
                ThreadPoolConst.IO_BOUND_QUEUE_CAPACITY,
                CORE_TASK);
    }

    private ThreadPoolTaskExecutor create(int corePoolSize,
                                          int maxPoolSize,
                                          int keepAliveSeconds,
                                          int queueCapacity,
                                          String threadNamePrefix) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setQueueCapacity(queueCapacity);
        executor.setRejectedExecutionHandler(ThreadPoolConst.REJECTED_EXECUTION_HANDLER);
        // 线程名称前缀
        executor.setThreadNamePrefix(threadNamePrefix + Separator.UNDERSCORE);
        // 初始化
        executor.initialize();
        return executor;
    }
}
