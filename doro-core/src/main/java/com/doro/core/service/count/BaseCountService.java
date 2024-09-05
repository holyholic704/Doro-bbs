package com.doro.core.service.count;

import com.doro.api.common.CountService;
import com.doro.cache.api.MyLock;
import com.doro.cache.utils.LockUtil;
import com.doro.cache.utils.RedisUtil;
import com.doro.common.constant.CacheKey;
import com.doro.common.constant.CommonConst;
import com.doro.common.constant.LockKey;
import com.doro.common.constant.UpdateCount;
import com.doro.mq.producer.UpdateCommentsProducer;
import org.redisson.api.RMap;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.Optional;

/**
 * @author jiage
 */
public abstract class BaseCountService implements CountService, BeanNameAware {

    protected String cachePrefix;

    @Autowired
    private UpdateCommentsProducer producer;
    @Autowired
    private ThreadPoolTaskExecutor coreIoTask;

    @Override
    public void setBeanName(String s) {
        this.cachePrefix = s;
    }

    @Override
    public Long getCount(long id) {
        Long result;
        return (result = getCountFromCache(id)) != null ? result : initCache(id, 0);
    }

    @Override
    public Long getCountFromCache(long id) {
        String cacheKey = cachePrefix + id;

        List<?> result = RedisUtil.initBatch(cacheKey)
                .expire(CommonConst.COMMON_CACHE_DURATION)
                .mapGet(UpdateCount.COUNT_KEY)
                .execute();

        if (result.get(1) != null) {
            return Long.parseLong(String.valueOf(result.get(1)));
        }
        return null;
    }

    @Override
    public void incrCount(long id) {
        updateCount(id, 1);
    }

    @Override
    public void decrCount(long id) {
        updateCount(id, -1);
    }

    @Override
    public void updateCount(long id, long add) {
        coreIoTask.execute(() -> updateAndSendMessage(id, add));
    }

    private void updateAndSendMessage(long id, long add) {
        initCache(id, add);
        // 减少针对同一 key 的重复消息，添加失败表示消息还未被消费
        String cacheKey = cachePrefix + id;
        if (RedisUtil.createBucket(CacheKey.COUNT_STILL_NOT_CONSUMED_PREFIX + cacheKey).setIfAbsent(0, CommonConst.COMMON_CACHE_DURATION)) {
            producer.send(cacheKey, 5);
        }
    }

    private Long initCache(long id, long add) {
        String cacheKey = cachePrefix + id;
        try (MyLock lock = LockUtil.lock(LockKey.INIT_COUNT_CACHE_PREFIX + id, CommonConst.COMMON_LOCK_LEASE_SECONDS)) {
            RMap<String, Long> rMap = RedisUtil.createMap(cacheKey);
            if (rMap.isExists()) {
                return rMap.addAndGet(UpdateCount.COUNT_KEY, add);
            }
            long last = Optional.ofNullable(getCountFromDatabase(id)).orElse(0L);
            RedisUtil.initBatch(cacheKey)
                    .mapPut(UpdateCount.LAST_UPDATE_KEY, last)
                    .mapPut(UpdateCount.COUNT_KEY, last + add)
                    .expire(CommonConst.COMMON_CACHE_DURATION)
                    .execute();
            return last + add;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void correctCount(long id) {
        String cacheKey = cachePrefix + id;
        RedisUtil.delete(cacheKey);
        updateCount(id, 0);
    }
}
