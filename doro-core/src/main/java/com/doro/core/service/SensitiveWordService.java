package com.doro.core.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.orm.bean.SensitiveWord;
import com.doro.orm.mapper.SensitiveWordMapper;
import org.springframework.stereotype.Service;

/**
 * @author jiage
 */
@Service
public class SensitiveWordService  extends ServiceImpl<SensitiveWordMapper, SensitiveWord> {
}
