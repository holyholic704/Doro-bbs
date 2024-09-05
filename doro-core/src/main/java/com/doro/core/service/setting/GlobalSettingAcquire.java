package com.doro.core.service.setting;

import cn.hutool.core.collection.CollUtil;
import com.doro.api.orm.GlobalSettingService;
import com.doro.bean.GlobalSettingBean;
import com.doro.cache.api.MyLock;
import com.doro.cache.utils.LockUtil;
import com.doro.cache.utils.MultiCacheUtil;
import com.doro.common.constant.CacheKey;
import com.doro.common.constant.LockKey;
import com.doro.common.exception.SystemException;
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
        Map<String, String> map = MultiCacheUtil.get(CacheKey.GLOBAL_SETTING);
        // 集合不为空，就可认为该字段一定存在
        if (map == null) {
            try (MyLock lock = LockUtil.lock(LockKey.INIT_GLOBAL_SETTING)) {
                if ((map = MultiCacheUtil.get(CacheKey.GLOBAL_SETTING)) == null) {
                    map = init();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        List<GlobalSettingBean> globalSettingBeanList = globalSettingService.getAllSetting();
        // 不可能出现没有数据的情况
        if (CollUtil.isEmpty(globalSettingBeanList)) {
            throw new SystemException("系统异常");
        }

        Map<String, String> map = globalSettingBeanList
                .stream()
                .collect(Collectors.toMap(GlobalSettingBean::getK, GlobalSettingBean::getV));
        MultiCacheUtil.put(CacheKey.GLOBAL_SETTING, map);
        return map;
    }
}
