package com.doro.core.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.orm.bean.GlobalSetting;
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

    /**
     * 获取全部
     */
    public List<GlobalSetting> getAll() {
        return this.list();
    }

    /**
     * 批量删除指定的 ID 集合
     */
    public boolean deleteByIdList(Collection<?> idCollection) {
        return this.removeByIds(idCollection);
    }

    /**
     * 批量保存
     */
    public boolean saveList(Collection<GlobalSetting> list) {
        return this.saveBatch(list);
    }

    /**
     * 版本号更新
     */
    public boolean updateVersion() {
        return this.update(new LambdaUpdateWrapper<GlobalSetting>()
                .eq(GlobalSetting::getK, "VERSION")
                .setSql("v = v + 1"));
    }
}
