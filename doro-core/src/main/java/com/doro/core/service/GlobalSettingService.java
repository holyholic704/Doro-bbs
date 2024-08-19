package com.doro.core.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.bean.setting.GlobalSetting;
import com.doro.orm.mapper.GlobalSettingMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 全局配置服务
 *
 * @author jiage
 */
@Service
public class GlobalSettingService extends ServiceImpl<GlobalSettingMapper, GlobalSetting> {

    public List<GlobalSetting> getAll() {
        return this.list();
    }

    public boolean deleteByIdList(Collection<?> idCollection) {
        return this.removeByIds(idCollection);
    }

    public boolean saveList(Collection<GlobalSetting> list) {
        return this.saveBatch(list);
    }

    public boolean updateVersion() {
        return this.update(new LambdaUpdateWrapper<GlobalSetting>()
                .eq(GlobalSetting::getK, "VERSION")
                .setSql("v = v + 1"));
    }
}
