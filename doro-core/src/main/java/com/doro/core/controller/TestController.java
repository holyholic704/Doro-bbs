package com.doro.core.controller;

import com.doro.api.service.TestService;
import com.doro.common.base.res.Response;
import com.doro.common.enumeration.ResponseEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ServiceLoader;

@RestController
public class TestController {

    @DubboReference
    TestService testService;

    @GetMapping("test")
    public Response test() {
        ServiceLoader<HelloService> serviceLoader = ServiceLoader.load(HelloService.class);
        serviceLoader.forEach(HelloService::say);
        return Response.ofEnum(ResponseEnum.ERROR);
    }

    public static void main(String[] args) {
        ServiceLoader<HelloService> serviceLoader = ServiceLoader.load(HelloService.class);
        serviceLoader.forEach(HelloService::say);
    }
}
