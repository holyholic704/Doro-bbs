package com.doro.core.service.comment;


import com.doro.cache.utils.RedisUtil;
import org.redisson.api.RSet;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jiage
 */
@Component
public class MergeUpdate {

    private final RSet<String> set = RedisUtil.createSet("test");
    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);

    public void addTask(String key) {
        set.add(key);
    }

    public void test() {
        executor.scheduleAtFixedRate(() -> {
            if (!set.isEmpty()) {
                Set<String> s = set.removeRandom(100);

            }
            System.out.println("done");
        }, 1, 5, TimeUnit.SECONDS);
    }
}
