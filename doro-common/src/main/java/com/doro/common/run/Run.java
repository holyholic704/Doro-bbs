package com.doro.common.run;

import com.doro.common.api.Runner;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 启动加载
 *
 * @author jiage
 */
@Component
public class Run implements ApplicationRunner {

    private final List<Runner> runnerList;

    public Run(List<Runner> runnerList) {
        this.runnerList = runnerList;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (Runner runner : runnerList) {
            runner.run();
        }
    }
}
