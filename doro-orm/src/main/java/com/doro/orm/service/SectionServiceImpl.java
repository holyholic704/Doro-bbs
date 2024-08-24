package com.doro.orm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

    @Override
    public boolean saveSection(SectionBean sectionBean) {
        return this.save(sectionBean);
    }

    @Override
    public boolean hasName(String name) {
        return this.count(new LambdaQueryWrapper<SectionBean>()
                .eq(SectionBean::getName, name)) > 0;
    }

    @Override
    public boolean hasSection(Long id) {
        return this.count(new LambdaQueryWrapper<SectionBean>()
                .eq(SectionBean::getId, id)) > 0;
    }
}
