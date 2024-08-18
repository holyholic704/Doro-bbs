package com.doro.core.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.github.yitter.idgen.YitIdHelper;
import org.springframework.stereotype.Component;

/**
 * Mybatis Plus 主键生成
 *
 * @author jiage
 */
@Component
public class CustomIdGenerator implements IdentifierGenerator {

    @Override
    public Long nextId(Object entity) {
        return YitIdHelper.nextId();
    }
}
