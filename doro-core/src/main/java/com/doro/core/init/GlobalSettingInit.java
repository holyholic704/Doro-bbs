package com.doro.core.init;

import cn.hutool.core.map.MapUtil;
import com.doro.bean.setting.GlobalSetting;
import com.doro.cache.utils.LockUtil;
import com.doro.common.api.MyLock;
import com.doro.common.constant.Settings;
import com.doro.core.service.GlobalSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Component
@DependsOn("lockUtil")
public class GlobalSettingInit {

    private final PlatformTransactionManager transactionManager;
    private final RedisTemplate<String, Object> redisTemplate;
    private final GlobalSettingService globalSettingService;

    @Autowired
    public GlobalSettingInit(PlatformTransactionManager transactionManager, RedisTemplate<String, Object> redisTemplate, GlobalSettingService globalSettingService) {
        this.transactionManager = transactionManager;
        this.redisTemplate = redisTemplate;
        this.globalSettingService = globalSettingService;
    }

    @PostConstruct
    private void run() {
        MyLock lock = LockUtil.tryLock("INIT_GLOBAL_SETTING");
        if (lock != null) {
            this.check();
            lock.unlockAsync();
        }
    }

    private void check() {
        Field[] fields = Settings.class.getDeclaredFields();
        Map<String, Field> fieldMap = new HashMap<>(fields.length);
        for (Field field : fields) {
            field.setAccessible(true);
            fieldMap.put(field.getName(), field);
        }

        Map<String, GlobalSetting> globalSettingMap = globalSettingService.getAllMap();

        filterMap(fieldMap, globalSettingMap);
        doInsertOrDelete(fieldMap, globalSettingMap);
    }

    // TODO 缓存预热
    private void addCache() {
//        List<GlobalSetting> globalSettingList = globalSettingService.getAll();
//        Map<String, Object> settingMap = new HashMap<>();
//        for (GlobalSetting setting : globalSettingList) {
//
//        }
//        redisTemplate.opsForHash().putAll();
    }

    private void doInsertOrDelete(Map<String, Field> fieldMap, Map<String, GlobalSetting> globalSettingMap) {
        // 使用编程式事务：方法可以设置为 private，避免自调用失效，事务更小
        TransactionStatus status = null;

        if (MapUtil.isNotEmpty(fieldMap)) {
            status = this.createStatus(status);
            Collection<Field> fieldCollection = fieldMap.values();
            List<GlobalSetting> list = new ArrayList<>();
            for (Field field : fieldCollection) {
                try {
                    list.add(new GlobalSetting()
                            .setK(field.getName())
                            .setV(String.valueOf(field.get(null)))
                            .setType(field.getType().getSimpleName()));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            globalSettingService.saveList(list);
        }

        if (MapUtil.isNotEmpty(globalSettingMap)) {
            status = this.createStatus(status);
            globalSettingService.deleteByIdList(globalSettingMap.values().stream().map(GlobalSetting::getId).collect(Collectors.toList()));
        }

        if (status != null) {
            transactionManager.commit(status);
        }
    }

    private TransactionStatus createStatus(TransactionStatus status) {
        return status != null ? status : transactionManager.getTransaction(new DefaultTransactionDefinition());
    }

    private void filterMap(Map<String, Field> fieldMap, Map<String, GlobalSetting> globalSettingMap) {
        if (MapUtil.isNotEmpty(globalSettingMap)) {
            Iterator<Map.Entry<String, GlobalSetting>> iterator = globalSettingMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, GlobalSetting> entry = iterator.next();
                // 只关心字段有没有，不关心值是否相同
                if (fieldMap.containsKey(entry.getKey())) {
                    iterator.remove();
                    fieldMap.remove(entry.getKey());
                }
            }
        }
    }
}
