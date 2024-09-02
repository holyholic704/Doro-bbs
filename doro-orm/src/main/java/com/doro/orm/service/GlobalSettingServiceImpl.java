package com.doro.orm.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.api.orm.GlobalSettingService;
import com.doro.api.bean.GlobalSettingBean;
import com.doro.orm.mapper.GlobalSettingMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 全局配置
 *
 * @author jiage
 */
@Service
class GlobalSettingServiceImpl extends ServiceImpl<GlobalSettingMapper, GlobalSettingBean> implements GlobalSettingService {

    @Override
    public List<GlobalSettingBean> getAllSetting() {
        return this.list();
    }

    @Override
    public boolean deleteSettingByIds(Collection<?> idCollection) {
        return this.removeByIds(idCollection);
    }

    @Override
    public boolean saveSettingList(Collection<GlobalSettingBean> list) {
        return this.saveBatch(list);
    }

    @Override
    public boolean updateVersion() {
        return this.update(new LambdaUpdateWrapper<GlobalSettingBean>()
                .eq(GlobalSettingBean::getK, "VERSION")
                .setSql("v = v + 1"));
    }
}
