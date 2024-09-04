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

import java.util.List;

/**
 * @author jiage
 */
public abstract class BaseCountService implements CountService, BeanNameAware {

    protected String cachePrefix;

    @Autowired
    private UpdateCommentsProducer producer;

    @Override
    public void setBeanName(String s) {
        this.cachePrefix = s;
    }

    abstract protected Long getFromDatabaseNullable(long id);

    @Override
    public long getCountFromDatabase(long id) {
        Long count = getFromDatabaseNullable(id);
        return count != null ? count : UpdateCount.ERROR_NO_DATA;
    }

    @Override
    public long getCount(Long id) {
        long result;
        return (result = getCountFromCache(id)) != UpdateCount.ERROR_NO_DATA ? result : initCache(id, 0);
    }

    @Override
    public long getCountFromCache(Long id) {
        String cacheKey = cachePrefix + id;

        List<?> result = RedisUtil.initBatch(cacheKey)
                .expire(CommonConst.COMMON_CACHE_DURATION)
                .mapGet(UpdateCount.COUNT_KEY)
                .execute();

        if (result.get(1) != null) {
            return Long.parseLong(String.valueOf(result.get(1)));
        }
        return UpdateCount.ERROR_NO_DATA;
    }

    @Override
    public void incrCount(Long id) {
        updateAndSendMessage(id, 1);
    }

    @Override
    public void decrCount(Long id) {
        updateAndSendMessage(id, -1);
    }

    private void updateAndSendMessage(Long id, int add) {
        initCache(id, add);
        // 减少针对同一 key 的重复消息，添加失败表示消息还未被消费
        String cacheKey = cachePrefix + id;
        if (RedisUtil.createSet(CacheKey.COUNT_STILL_NOT_CONSUMED).add(cacheKey)) {
            producer.send(cacheKey, CommonConst.COMMON_CACHE_DURATION.getSeconds() / 2);
        }
    }

    private long initCache(Long id, long add) {
        String cacheKey = cachePrefix + id;
        try (MyLock lock = LockUtil.lock(LockKey.INIT_COUNT_CACHE_PREFIX + id, CommonConst.COMMON_LOCK_LEASE_SECONDS)) {
            RMap<String, String> rMap = RedisUtil.createMap(cacheKey);
            if (rMap.isExists()) {
                return Long.parseLong(rMap.addAndGet(cacheKey, add));
            }
            long last = getCountFromDatabase(id);
            if (last != UpdateCount.ERROR_NO_DATA) {
                RedisUtil.initBatch(cacheKey)
                        .mapPut(UpdateCount.LAST_UPDATE_KEY, last)
                        .mapPut(UpdateCount.COUNT_KEY, last + add)
                        .expire(CommonConst.COMMON_CACHE_DURATION)
                        .execute();
                return last + add;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return UpdateCount.ERROR_NO_DATA;
    }
}
