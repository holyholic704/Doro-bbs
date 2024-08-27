package com.doro.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author jiage
 */
@Configuration
public class ThreadPoolConfig {

    @Bean(name = "coreTask")
    public ThreadPoolTaskExecutor coreTask() {
        // 核心线程数
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
        // 最大线程数
        int maxPoolSize = corePoolSize * 2;
        // 非核心线程闲置超时时长
        int keepAliveSeconds = 60;
        // 阻塞队列长度
        int queueCapacity = corePoolSize * 100;

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setQueueCapacity(queueCapacity);
        // 线程名称前缀
        executor.setThreadNamePrefix("CoreTask-");
        // 初始化
        executor.initialize();
        return executor;
    }
}
