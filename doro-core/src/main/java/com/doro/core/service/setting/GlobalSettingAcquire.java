package com.doro.core.service.setting;

import cn.hutool.core.collection.CollUtil;
import com.doro.cache.utils.MultiCacheUtil;
import com.doro.common.constant.CacheConstant;
import com.doro.core.exception.SystemException;
import com.doro.orm.service.GlobalSettingService;
import com.doro.orm.bean.GlobalSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 获取全局配置
 *
 * @author jiage
 */
@Component
public class GlobalSettingAcquire {

    private static GlobalSettingService globalSettingService;

    @Autowired
    public void setGlobalSettingService(GlobalSettingService globalSettingService) {
        GlobalSettingAcquire.globalSettingService = globalSettingService;
    }

    /**
     * 获取配置的值
     */
    public static <T> T get(G_Setting gSetting) {
        Map<String, String> map = MultiCacheUtil.get(CacheConstant.GLOBAL_SETTING);
        // 集合不为空，就可认为该字段一定存在
        if (map != null) {
            return convert(map.get(gSetting.name()), gSetting.getConvert());
        }
        map = init();
        return convert(map.get(gSetting.name()), gSetting.getConvert());
    }

    @SuppressWarnings("unchecked")
    private static <T> T convert(String value, Function<String, ?> function) {
        return (T) function.apply(value);
    }

    /**
     * 初始化，将数据库中的数据添加到缓存中
     */
    public static Map<String, String> init() {
        List<GlobalSetting> globalSettingList = globalSettingService.getAll();
        // 不可能出现没有数据的情况
        if (CollUtil.isEmpty(globalSettingList)) {
            throw new SystemException("系统异常");
        }

        Map<String, String> map = globalSettingList
                .stream()
                .collect(Collectors.toMap(GlobalSetting::getK, GlobalSetting::getV));
        MultiCacheUtil.put(CacheConstant.GLOBAL_SETTING, map);
        return map;
    }
}
