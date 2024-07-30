package com.doro.core.controller;

import com.doro.api.TestService;
import com.doro.core.service.HelloService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ServiceLoader;

@RestController
@RefreshScope
public class TestController {

    @Value("${hello.say}")
    private String say;

    @DubboReference
    TestService testService;

    @GetMapping("test")
    public String test() {
        ServiceLoader<HelloService> serviceLoader = ServiceLoader.load(HelloService.class);
        serviceLoader.forEach(HelloService::say);
        return say + testService.getStr();
    }

    public static void main(String[] args) {
        ServiceLoader<HelloService> serviceLoader = ServiceLoader.load(HelloService.class);
        serviceLoader.forEach(HelloService::say);
    }
}
