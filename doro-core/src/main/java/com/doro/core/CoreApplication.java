package com.doro.core;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDubbo
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.doro.orm.mapper")
@ComponentScan({"com.doro.core.*", "com.doro.*.config", "com.doro.*.utils"})
public class CoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }

}
