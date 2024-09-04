package com.doro.core.service.count;

import cn.hutool.core.util.ObjectUtil;
import com.doro.api.common.CountService;
import com.doro.api.dto.CountSnapshot;
import com.doro.cache.api.MyLock;
import com.doro.cache.utils.LockUtil;
import com.doro.cache.utils.RedisUtil;
import com.doro.common.constant.CacheKey;
import com.doro.common.constant.CommonConst;
import com.doro.common.constant.LockKey;
import com.doro.common.enumeration.UpdateCount;
import com.doro.mq.producer.UpdateCommentsProducer;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author jiage
 */
public abstract class BaseCountService implements CountService, BeanNameAware {

    protected String cachePrefix;

    @Override
    public void setBeanName(String s) {
        this.cachePrefix = s;
    }

    abstract long getCountFromDatabase(long id);

    @Override
    public long getCount(Long id) {
        Long result = getCountFromCache(id);

        if (result != null) {
            return result;
        } else {
            long count = getCountFromDatabase(id);
            initCache(cachePrefix + id, count, count);
            return count;
        }
    }

    @Override
    public Long getCountFromCache(Long id) {
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
    public void incrCount(Long id, int add) {
        updateAndSendMessage(id, 1);
    }

    @Override
    public void decrCount(Long id, int add) {
        updateAndSendMessage(id, -1);
    }

    @Autowired
    private UpdateCommentsProducer producer;

    private void updateAndSendMessage(Long id, int add) {
        CountSnapshot snapshot = updateCount(id, add);
        // 减少针对同一 key 的重复消息，添加失败表示消息还未被消费
        if (RedisUtil.createSet(CacheKey.COUNT_STILL_NOT_CONSUMED).add(cachePrefix + id)) {
            producer.send(ObjectUtil.serialize(snapshot), CommonConst.COMMON_CACHE_DURATION.getSeconds() / 2);
        }
    }

    private CountSnapshot updateCount(Long id, int add) {
        String cacheKey = cachePrefix + id;

        List<?> responses = RedisUtil.initBatch(cacheKey)
                .expire(CommonConst.COMMON_CACHE_DURATION)
                .isExists()
                .execute();

        boolean isExist = (Boolean) responses.get(1);

        if (!isExist) {
            long comments = getCountFromDatabase(id);

            boolean isSuccess = initCache(cacheKey, comments, comments + add);
            if (isSuccess) {
                return new CountSnapshot(cachePrefix,
                        id,
                        comments,
                        comments + add);
            }
        }

        // 一般来说执行这步时缓存不会失效，只要保证缓存没有被删除即可
        responses = RedisUtil.initBatch(cacheKey)
                .mapGet(UpdateCount.LAST_UPDATE_KEY)
                .mapAddAndGet(UpdateCount.COUNT_KEY, add)
                .execute();
        long last = Long.parseLong(String.valueOf(responses.get(0)));
        long count = Long.parseLong(String.valueOf(responses.get(1)));
        return new CountSnapshot(cachePrefix,
                id,
                last,
                count);
    }

    private boolean initCache(String cacheKey, long last, long count) {
        // 只允许同一时间只有一个线程可以更新缓存
        try (MyLock lock = LockUtil.lock(LockKey.INIT_COUNT_CACHE_PREFIX + cacheKey, CommonConst.COMMON_LOCK_LEASE_SECONDS)) {
            List<?> result = RedisUtil.initBatch(cacheKey)
                    .mapPutIfAbsent(UpdateCount.LAST_UPDATE_KEY, last)
                    .mapPutIfAbsent(UpdateCount.COUNT_KEY, count)
                    .expire(CommonConst.COMMON_CACHE_DURATION)
                    .execute();
            return result.get(0) == null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

//    public boolean updateCache(Long id, long newValue) {
//        String cacheKey = cachePrefix + id;
//        List<?> responses = RedisUtil.initBatch(cacheKey)
//                .expire(CommonConst.COMMON_CACHE_DURATION)
//                .isExists()
//                .execute();
//
//        boolean isExist = (Boolean) responses.get(1);
//        if (isExist) {
//        }
//    }

    @Override
    public boolean delCache(Long id) {
        return RedisUtil.delete(cachePrefix + id);
    }
}
