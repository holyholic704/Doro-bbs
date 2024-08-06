package com.doro.core.controller;

import com.doro.api.service.TestService;
import com.doro.common.base.res.Response;
import com.doro.common.enumeration.ResponseEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @DubboReference
    TestService testService;

    @GetMapping("test")
    public Response test() {
        System.out.println(1 / 0);
        return Response.ofEnum(ResponseEnum.ERROR);
    }
}
