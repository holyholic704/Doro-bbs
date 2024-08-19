package com.doro.cache.flush;

import com.doro.cache.utils.LocalCacheUtil;
import org.redisson.api.listener.MessageListener;

/**
 * 消息监听器，刷新本地缓存
 *
 * @author jiage
 */
class FlushLocalCacheListener implements MessageListener<String> {

    @Override
    public void onMessage(CharSequence channel, String key) {
        LocalCacheUtil.remove(key);
    }
}
