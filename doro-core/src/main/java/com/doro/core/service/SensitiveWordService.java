package com.doro.core.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.orm.bean.SensitiveWordBean;
import com.doro.orm.mapper.SensitiveWordMapper;
import org.springframework.stereotype.Service;

/**
 * 敏感词
 *
 * @author jiage
 */
@Service
public class SensitiveWordService extends ServiceImpl<SensitiveWordMapper, SensitiveWordBean> {
}
