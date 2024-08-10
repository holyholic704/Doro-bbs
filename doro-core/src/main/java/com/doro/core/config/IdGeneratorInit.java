package com.doro.core.config;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class IdGeneratorInit implements ApplicationRunner {

    @Value("${IdGenerator.workId}")
    private short workId;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        IdGeneratorOptions options = new IdGeneratorOptions(workId);
        YitIdHelper.setIdGenerator(options);
        System.out.println("id 初始化");
    }
}
