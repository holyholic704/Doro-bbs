package com.doro.orm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doro.bean.setting.GlobalSetting;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

public interface GlobalSettingMapper extends BaseMapper<GlobalSetting> {

    @MapKey("k")
    @Select("SELECT id, k, v FROM global_setting;")
    Map<String, GlobalSetting> getAllMap();
}
