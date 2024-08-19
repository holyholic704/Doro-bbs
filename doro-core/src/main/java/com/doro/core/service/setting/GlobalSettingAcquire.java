package com.doro.core.service.setting;

import com.doro.bean.setting.GlobalSetting;
import com.doro.cache.utils.MultiCacheUtil;
import com.doro.common.constant.CacheConstant;
import com.doro.core.exception.SystemException;
import com.doro.core.service.GlobalSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jiage
 */
@Component
public class GlobalSettingAcquire {

    private static GlobalSettingService globalSettingService;

    @Autowired
    public void setGlobalSettingService(GlobalSettingService globalSettingService) {
        GlobalSettingAcquire.globalSettingService = globalSettingService;
    }

    public static <T> T get(G_Setting gSetting) {
        Map<String, String> map = MultiCacheUtil.get(CacheConstant.GLOBAL_SETTING);
        if (map != null) {
            return convert(map.get(gSetting.name()), gSetting.getFunction());
        }
        map = init();
        return convert(map.get(gSetting.name()), gSetting.getFunction());
    }

    @SuppressWarnings("unchecked")
    private static <T> T convert(String value, Function<String, ?> function) {
        return (T) function.apply(value);
    }

    public static Map<String, String> init() {
        List<GlobalSetting> globalSettingList = globalSettingService.getAll();
        if (globalSettingList == null) {
            throw new SystemException("系统异常");
        }

        Map<String, String> map = globalSettingList
                .stream()
                .collect(Collectors.toMap(GlobalSetting::getK, GlobalSetting::getV));
        MultiCacheUtil.put(CacheConstant.GLOBAL_SETTING, map);
        return map;
    }
}
