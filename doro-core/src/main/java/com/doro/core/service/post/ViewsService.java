package com.doro.core.service.post;

import com.doro.cache.api.MyLock;
import com.doro.cache.utils.LockUtil;
import com.doro.cache.utils.RedisUtil;
import com.doro.common.constant.CacheKey;
import com.doro.common.constant.LockKey;
import com.doro.common.constant.PostConst;
import com.doro.orm.api.PostService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.redisson.api.RBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

/**
 * @author jiage
 */
@Service
public class ViewsService {

    private final PostService postService;

    @Autowired
    public ViewsService(PostService postService) {
        this.postService = postService;
    }

    Cache<Long, Long> cache = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofSeconds(10)).build();

    public void incrViews(Long postId) {
        // TODO 键过期自动刷新到数据库？定时任务？
        String cacheKey = CacheKey.POST_VIEWS_PREFIX + postId;

        RBatch rBatch = RedisUtil.createBatch();
        rBatch.getAtomicLong(cacheKey).expireIfSetAsync(PostConst.CACHE_VIEWS_DURATION);
        rBatch.getAtomicLong(cacheKey).getAsync();
        List<?> result = rBatch.execute().getResponses();

        Long cacheViews = (Long) result.get(1);

        if (cacheViews != null && cacheViews == 0) {
            String lockKey = LockKey.INIT_VIEWS_CACHE_PREFIX + postId;
            MyLock lock = LockUtil.lock(lockKey, 10);
            // 二次检查
            Long views = cache.getIfPresent(postId);
            if (views == null) {
                // TODO 为 null，跳过
                views = postService.getPostViews(postId);
                if (views != null) {
                    cache.put(postId, views);
                    rBatch = RedisUtil.createBatch();
                    rBatch.getAtomicLong(cacheKey).compareAndSetAsync(0, views);
                    rBatch.getAtomicLong(cacheKey).expireIfNotSetAsync(PostConst.CACHE_VIEWS_DURATION);
                    result = rBatch.execute().getResponses();
                    Boolean isSuccess = (Boolean) result.get(0);
                    
                    if (isSuccess!=null && isSuccess) {
                        LockUtil.unlock(lock);
                        return;
                    }
                }
            }
            LockUtil.unlock(lock);
        }
        RedisUtil.createAtomicLong(cacheKey).incrementAndGet();
    }

    private Long getCacheViews(Long postId) {
        String cacheKey = CacheKey.POST_VIEWS_PREFIX + postId;
        RBatch rBatch = RedisUtil.createBatch();
        // 为浏览量缓存延长超时时间
        rBatch.getAtomicLong(cacheKey).expireIfSetAsync(PostConst.CACHE_VIEWS_DURATION);
        // 获取浏览量
        rBatch.getAtomicLong(cacheKey).getAsync();
        List<?> result = rBatch.execute().getResponses();
        return (Long) result.get(1);
    }
}
