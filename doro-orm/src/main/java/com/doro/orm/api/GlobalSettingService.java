package com.doro.orm.api;

import com.doro.orm.bean.GlobalSettingBean;

import java.util.Collection;
import java.util.List;

/**
 * 全局配置
 *
 * @author jiage
 */
public interface GlobalSettingService {

    /**
     * 获取全部
     */
    List<GlobalSettingBean> getAllSetting();

    /**
     * 批量删除指定的 ID 集合
     */
    boolean deleteSettingByIds(Collection<?> idCollection);

    /**
     * 批量保存
     */
    boolean saveSettingList(Collection<GlobalSettingBean> list);

    /**
     * 版本号更新
     */
    boolean updateVersion();
}
