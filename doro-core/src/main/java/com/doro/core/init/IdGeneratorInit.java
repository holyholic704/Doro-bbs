package com.doro.core.init;

import com.doro.common.api.Runner;
import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 初始化分布式 ID
 *
 * @author jiage
 */
@Component
public class IdGeneratorInit implements Runner {

    @Value("${worker-id}")
    private short workerId;

    @Override
    public void run() {
        IdGeneratorOptions options = new IdGeneratorOptions(workerId);
        YitIdHelper.setIdGenerator(options);
    }
}
