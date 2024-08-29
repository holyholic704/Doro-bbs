package com.doro.xxljob.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * @author jiage
 */
@Component
public class TestJob {

    @XxlJob("woc")
    public void test() {
        System.out.println("执行了");
    }
}
