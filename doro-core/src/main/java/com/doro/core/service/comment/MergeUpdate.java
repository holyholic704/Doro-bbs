package com.doro.core.service.comment;


import com.doro.cache.utils.RedisUtil;
import com.doro.mq.producer.UpdateCommentsProducer;
import org.redisson.api.RSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jiage
 */
@Component
@DependsOn("redisUtil")
public class MergeUpdate {

    private final UpdateCommentsProducer producer;
    private final ScheduledThreadPoolExecutor executor;
    private final RSet<String> set;

    @Autowired
    public MergeUpdate(UpdateCommentsProducer producer) {
        this.producer = producer;
        executor = new ScheduledThreadPoolExecutor(5);
        set = RedisUtil.createSet("test");
        test();
    }

    public void addTask(String key) {
        set.add(key);
    }

    public void test() {
        // 相当于 5 秒内只执行一次
        executor.scheduleAtFixedRate(() -> {
            try {
                if (!set.isEmpty()) {
                    List<?> result = RedisUtil.initBatch("test")
                            .setReadAll()
                            .delete()
                            .execute();
                    Set<String> currentSet = (Set<String>) result.get(0);
                }
            } catch (Exception e) {

            }
        }, 1, 5, TimeUnit.SECONDS);
    }
}
