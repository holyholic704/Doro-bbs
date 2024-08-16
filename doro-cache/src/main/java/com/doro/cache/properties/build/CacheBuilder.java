package com.doro.cache.properties.build;

import cn.hutool.core.util.StrUtil;
import com.doro.cache.properties.CacheProperties;
import com.doro.cache.properties.LocalCacheProperties;
import com.doro.cache.utils.LocalCacheUtil;

/**
 * @author jiage
 */
public class CacheBuilder {

    public void test(CacheProperties cacheProperties) {
        switch (cacheProperties.getCacheType()) {
            case LOCAL:
                break;
            case REMOTE:
                break;
            default:
                break;
        }
    }

    private void buildLocal(CacheProperties cacheProperties) {
        LocalCacheUtil.putLocal();
    }

    private void composeLocalKey(CacheProperties cacheProperties) {
        String cacheKey = cacheProperties.getKey();
        new LocalCacheProperties()
                .setMaxSize(cacheProperties.getLocalMaxSize())
                .setExpireTime(cacheProperties.getLocalExpire())
                .setUnit(cacheProperties.getUnit())
        if (StrUtil.isNotEmpty(cacheProperties.getArea())) {


        }
    }
}
