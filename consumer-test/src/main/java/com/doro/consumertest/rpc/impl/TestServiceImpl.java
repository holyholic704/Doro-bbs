package com.doro.consumertest.rpc.impl;

import com.doro.api.TestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
@Slf4j
public class TestServiceImpl implements TestService {

    @Override
    public String getStr() {
        log.info("我被调用了");
        return "你成功了";
    }
}
