package com.doro.orm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.api.orm.SectionService;
import com.doro.bean.SectionBean;
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
    public boolean delSection(Long id) {
        return this.removeById(id);
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
