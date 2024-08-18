package com.doro.cache.listener;

import com.doro.cache.utils.LocalCacheUtil;
import org.redisson.api.listener.MessageListener;

/**
 * @author jiage
 */
public class DelMessageListener implements MessageListener<String> {

    @Override
    public void onMessage(CharSequence channel, String msg) {
        LocalCacheUtil.remove(msg);
    }
}
