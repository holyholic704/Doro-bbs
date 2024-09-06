package com.doro.web;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 应用！启动！！！
 *
 * @author jiage
 */
//@EnableDubbo
//@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.doro.orm.mapper")
@ComponentScan({"com.doro.*.*"})
@EnableMethodCache(basePackages = "com.doro")
public class DoroWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoroWebApplication.class, args);
    }

}
