package com.doro.core.init;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class IdGeneratorInit {

    @Value("${IdGenerator.workId}")
    private short workId;

    @PostConstruct
    private void run() {
        IdGeneratorOptions options = new IdGeneratorOptions(workId);
        YitIdHelper.setIdGenerator(options);
        System.out.println("id 初始化");
    }
}
