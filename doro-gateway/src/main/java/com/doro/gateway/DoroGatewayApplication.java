package com.doro.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class DoroGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoroGatewayApplication.class, args);
    }

}
