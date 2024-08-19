package com.doro.cache.listener;

import com.doro.cache.constant.CacheConstant;
import com.doro.cache.utils.LocalCacheUtil;
import org.redisson.api.listener.MessageListener;

/**
 * 消息监听器
 *
 * @author jiage
 */
public class MyMessageListener implements MessageListener<String> {

    @Override
    public void onMessage(CharSequence channel, String msg) {
        if (CacheConstant.DEL_LOCAL_CACHE_TOPIC.contentEquals(channel)) {
            LocalCacheUtil.remove(msg);
        }
    }
}
