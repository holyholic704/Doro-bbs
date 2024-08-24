package com.doro.orm.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.orm.api.SectionService;
import com.doro.orm.bean.SectionBean;
import com.doro.orm.mapper.SectionMapper;
import org.springframework.stereotype.Service;

/**
 * 版块
 *
 * @author jiage
 */
@Service
class SectionServiceImpl extends ServiceImpl<SectionMapper, SectionBean> implements SectionService {
}
