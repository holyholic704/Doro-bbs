package com.doro.core.service.count;

import com.doro.api.common.CountService;
import com.doro.api.mq.UpdateCountMqService;
import com.doro.cache.api.MyLock;
import com.doro.cache.utils.LockUtil;
import com.doro.cache.utils.RedisUtil;
import com.doro.common.constant.CacheKey;
import com.doro.common.constant.CommonConst;
import com.doro.common.constant.LockKey;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

/**
 * 计数
 *
 * @author jiage
 */
public abstract class BaseCountService implements CountService, BeanNameAware {

    @Autowired
    private UpdateCountMqService updateCountMqService;
    @Autowired
    private ThreadPoolTaskExecutor coreIoTask;

    /**
     * 缓存 key 前缀，并以此区分不同的实现
     */
    protected String cachePrefix;

    @Override
    public void setBeanName(String s) {
        this.cachePrefix = s;
    }

    /**
     * 获取计数
     * 先从缓存中查找，没有则初始化缓存
     *
     * @param id 主键
     * @return 对应主键的计数
     */
    @Override
    public Long getCount(long id) {
        Long result;
        return (result = getCountFromCache(id)) != null
                ? result
                : initCache(id, 0);
    }

    /**
     * 从缓存中获取计数
     *
     * @param id 主键
     * @return 对应主键的计数，没有缓存返回 null
     */
    @Override
    public Long getCountFromCache(long id) {
        return getAndExpire(getCacheKey(id));
    }

    /**
     * 自增
     *
     * @param id 主键
     */
    @Override
    public void incrCount(long id) {
        updateCount(id, 1);
    }

    /**
     * 自减
     *
     * @param id 主键
     */
    @Override
    public void decrCount(long id) {
        updateCount(id, -1);
    }

    /**
     * 更新计数并发送 MQ 消息
     *
     * @param id  主键
     * @param add 增加量
     */
    @Override
    public void updateCount(long id, long add) {
        // 异步执行
        coreIoTask.execute(() -> {
            Long count = initCache(id, add);
            // 减少针对同一 key 的重复消息，添加失败表示消息还未被消费
            String cacheKey = getCacheKey(id);
            if (RedisUtil.createBucket(CacheKey.COUNT_STILL_NOT_CONSUMED_PREFIX + cacheKey).setIfAbsent(0, CommonConst.COMMON_HALF_CACHE_DURATION)) {
                updateCountMqService.send(cachePrefix, id, count, 5);
            }
        });
    }

    /**
     * 更新数据库中的计数
     *
     * @param id 主键
     */
    protected abstract void updateDatabaseCount(long id, Long count);

    /**
     * 校正计数
     * 从数据库中统计并添加进缓存
     *
     * @param id 主键
     */
    @Override
    public boolean correctCount(long id, Long count) {
        try {
            updateDatabaseCount(id, count);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try (MyLock lock = LockUtil.lock(LockKey.INIT_COUNT_CACHE_PREFIX + id, CommonConst.COMMON_LOCK_LEASE_SECONDS)) {
            count = getCountFromDatabase(id);
            setAndExpire(getCacheKey(id), count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 初始化缓存
     *
     * @param id  主键
     * @param add 增加量
     * @return 计数
     */
    private Long initCache(long id, long add) {
        String cacheKey = getCacheKey(id);

        if (!isExistsAndExpire(cacheKey)) {
            // 同时只有一个线程能初始化缓存
            try (MyLock lock = LockUtil.lock(LockKey.INIT_COUNT_CACHE_PREFIX + id, CommonConst.COMMON_LOCK_LEASE_SECONDS)) {
                // 避免重复执行数据库的查询
                if (!isExistsAndExpire(cacheKey)) {
                    Long count = getCountFromDatabase(id);
                    // 计数的增减都在数据成功写入数据库之后，所以数据基本上都是有的，
                    if (count != null) {
                        return setAndExpire(cacheKey, count + 1);
                    }
                }
            } catch (Exception e) {
                // 正常来说不会出现异常
                e.printStackTrace();
            }
        }

        // 不用担心到这一步刚好缓存失效，之前的操作都有延长超时时间
        return RedisUtil.createAtomicLong(cacheKey).addAndGet(add);
    }

    /**
     * 判断缓存是否存在
     *
     * @param cacheKey 缓存 key
     * @return 缓存是否存在
     */
    private boolean isExistsAndExpire(String cacheKey) {
        return getAndExpire(cacheKey) != null;
    }

    /**
     * 从缓存中获取
     *
     * @param cacheKey 缓存 key
     * @return 计数，没有返回 null
     */
    private Long getAndExpire(String cacheKey) {
        // 获取缓存的同时延长超时时间
        // 延长时间在前，避免出现获取成功后立即超时的情况，导致延长时间失败（key 不存在）
        List<?> result = RedisUtil.initBatch(cacheKey)
                .expire(CommonConst.COMMON_CACHE_DURATION)
                .isExists()
                .atomicLongGet()
                .execute();

        // 不能用 get 方法判断是否存在缓存，计数可能存在为 0 的情况
        boolean isExists = (Boolean) result.get(1);

        if (isExists) {
            return (Long) result.get(2);
        }
        return null;
    }

    /**
     * 添加缓存
     *
     * @param cacheKey 缓存 key
     * @param count    待添加进缓存的计数
     * @return 计数
     */
    private long setAndExpire(String cacheKey, long count) {
        RedisUtil.initBatch(cacheKey)
                .atomicLongSet(count)
                .expire(CommonConst.COMMON_CACHE_DURATION)
                .execute();
        return count;
    }

    /**
     * 拼接缓存 key
     *
     * @param id 主键
     * @return 缓存 key
     */
    private String getCacheKey(long id) {
        return cachePrefix + id;
    }
}
