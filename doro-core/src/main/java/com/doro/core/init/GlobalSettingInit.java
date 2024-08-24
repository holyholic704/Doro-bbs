package com.doro.core.init;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.doro.cache.api.MyLock;
import com.doro.cache.utils.LockUtil;
import com.doro.common.api.Runner;
import com.doro.common.constant.LockConstant;
import com.doro.core.service.setting.G_Setting;
import com.doro.core.service.setting.GlobalSettingAcquire;
import com.doro.orm.api.GlobalSettingService;
import com.doro.orm.bean.GlobalSettingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 初始化全局配置
 *
 * @author jiage
 */
@Component
//@DependsOn("lockUtil")
public class GlobalSettingInit implements Runner {

    private final PlatformTransactionManager transactionManager;
    private final GlobalSettingService globalSettingService;

    @Autowired
    public GlobalSettingInit(PlatformTransactionManager transactionManager, GlobalSettingService globalSettingService) {
        this.transactionManager = transactionManager;
        this.globalSettingService = globalSettingService;
    }

    @Override
    public void run() {
        // 同一时间只允许一个节点可以进行初始化
        MyLock lock = LockUtil.tryLock(LockConstant.INIT_GLOBAL_SETTING);
        if (lock != null) {
            this.check();
            GlobalSettingAcquire.init();
            lock.unlockAsync();
        }
    }

    /**
     * 检查配置
     */
    private void check() {
        Map<String, G_Setting> globalSettingTemplateMap = Arrays.stream(G_Setting.values()).collect(Collectors.toMap(G_Setting::name, Function.identity()));

        Map<String, GlobalSettingBean> globalSettingMap = null;
        List<GlobalSettingBean> globalSettingBeanList = globalSettingService.getAllSetting();
        if (CollUtil.isNotEmpty(globalSettingBeanList)) {
            globalSettingMap = globalSettingBeanList.stream().collect(Collectors.toMap(GlobalSettingBean::getK, Function.identity()));
            // 过滤
            filterMap(globalSettingTemplateMap, globalSettingMap);
        }

        doInsertOrDelete(globalSettingTemplateMap, globalSettingMap);
    }

    /**
     * 新增或删除数据库中的配置
     */
    private void doInsertOrDelete(Map<String, G_Setting> gSettingMap, Map<String, GlobalSettingBean> globalSettingMap) {
        // 使用编程式事务：方法可以设置为 private，避免自调用失效，事务更小
        TransactionStatus status = null;

        // 添加数据库中没有的字段
        if (MapUtil.isNotEmpty(gSettingMap)) {
            status = this.createStatus(null);
            Collection<G_Setting> gSettings = gSettingMap.values();
            List<GlobalSettingBean> list = new ArrayList<>();
            for (G_Setting gSetting : gSettings) {
                list.add(new GlobalSettingBean()
                        .setK(gSetting.name())
                        .setV(String.valueOf(gSetting.getDefaultValue())));
            }

            globalSettingService.saveSettingList(list);
        }

        // 删除数据库中多余的字段
        if (MapUtil.isNotEmpty(globalSettingMap)) {
            status = this.createStatus(status);
            globalSettingService.deleteSettingByIds(globalSettingMap.values().stream().map(GlobalSettingBean::getId).collect(Collectors.toList()));
        }

        if (status != null) {
            // 更新版本
            globalSettingService.updateVersion();
            transactionManager.commit(status);
        }
    }

    private TransactionStatus createStatus(TransactionStatus status) {
        return status != null ? status : transactionManager.getTransaction(new DefaultTransactionDefinition());
    }

    /**
     * 字段过滤，保留数据库中没有或多余的字段
     */
    private void filterMap(Map<String, G_Setting> gSettingMap, Map<String, GlobalSettingBean> globalSettingMap) {
        Iterator<Map.Entry<String, GlobalSettingBean>> iterator = globalSettingMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, GlobalSettingBean> entry = iterator.next();
            // 只关心字段有没有，不关心值是否相同
            if (gSettingMap.containsKey(entry.getKey())) {
                iterator.remove();
                gSettingMap.remove(entry.getKey());
            }
        }
    }
}
