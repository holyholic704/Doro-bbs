package com.doro.cache.config;

import com.doro.common.constant.Separator;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // TODO 目前为单例部署
        config.useSingleServer()
                .setAddress("redis://" + host + Separator.COLON + port)
                .setPassword(password);
        return Redisson.create(config);
    }
}
