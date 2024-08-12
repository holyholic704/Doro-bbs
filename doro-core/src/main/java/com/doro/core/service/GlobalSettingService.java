package com.doro.core.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.bean.setting.GlobalSetting;
import com.doro.orm.mapper.GlobalSettingMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class GlobalSettingService extends ServiceImpl<GlobalSettingMapper, GlobalSetting> {

    public List<GlobalSetting> getAll() {
        return this.list();
    }

    public Map<String, GlobalSetting> getAllMap() {
        return this.getBaseMapper().getAllMap();
    }

    public boolean deleteByIdList(Collection<?> idCollection) {
        return this.removeByIds(idCollection);
    }

    public boolean saveList(Collection<GlobalSetting> list) {
        return this.saveBatch(list);
    }
}
