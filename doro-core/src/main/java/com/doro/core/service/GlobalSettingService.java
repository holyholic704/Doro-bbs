package com.doro.core.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.orm.bean.GlobalSettingBean;
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
public class GlobalSettingService extends ServiceImpl<GlobalSettingMapper, GlobalSettingBean> {

    /**
     * 获取全部
     */
    public List<GlobalSettingBean> getAll() {
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
    public boolean saveList(Collection<GlobalSettingBean> list) {
        return this.saveBatch(list);
    }

    /**
     * 版本号更新
     */
    public boolean updateVersion() {
        return this.update(new LambdaUpdateWrapper<GlobalSettingBean>()
                .eq(GlobalSettingBean::getK, "VERSION")
                .setSql("v = v + 1"));
    }
}
