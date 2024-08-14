package com.doro.cache.config;

import com.doro.common.constant.Separator;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Redisson 配置
 */
@Configuration
public class RedissonConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient() {
        return Redisson.create(initConfig());
    }

    /**
     * 初始化配置
     */
    private Config initConfig() {
        Config config = new Config();

        if (redisProperties.getCluster() != null) {
            config.useClusterServers()
                    .addNodeAddress(this.getNodeAddresses())
                    .setPassword(redisProperties.getPassword());
        } else if (redisProperties.getSentinel() != null) {
            // TODO Sentinel 支持
        } else {
            config.useSingleServer()
                    .setAddress("redis://" + redisProperties.getHost() + Separator.COLON + redisProperties.getPort())
                    .setPassword(redisProperties.getPassword());
        }
        return config;
    }

    /**
     * 获取集群的节点地址
     */
    private String[] getNodeAddresses() {
        List<String> clusterNodeList = redisProperties.getCluster().getNodes();
        String[] nodeAddresses = new String[clusterNodeList.size()];
        for (int i = 0; i < clusterNodeList.size(); i++) {
            nodeAddresses[i] = "redis://" + clusterNodeList.get(i);
        }
        return nodeAddresses;
    }
}
