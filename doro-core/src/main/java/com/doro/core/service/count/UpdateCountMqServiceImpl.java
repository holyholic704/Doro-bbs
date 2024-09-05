package com.doro.core.service.count;


import cn.hutool.core.map.MapUtil;
import com.doro.api.mq.UpdateCountMqService;
import com.doro.api.orm.CommentService;
import com.doro.api.orm.PostService;
import com.doro.cache.api.MyLock;
import com.doro.cache.utils.LockUtil;
import com.doro.cache.utils.RedisUtil;
import com.doro.common.constant.CacheKey;
import com.doro.common.constant.CommonConst;
import com.doro.common.constant.Separator;
import com.doro.common.constant.UpdateCount;
import com.doro.core.config.ThreadPoolConfig;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author jiage
 */
@Component
@DependsOn("redisUtil")
public class UpdateCountMqServiceImpl implements UpdateCountMqService {

    private final CommentService commentService;
    private final PostService postService;

    private final RSet<String> COUNT_STILL_NOT_CONSUMED = RedisUtil.createSet(CacheKey.COUNT_STILL_NOT_CONSUMED);

    @Autowired
    public UpdateCountMqServiceImpl(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @Async(ThreadPoolConfig.CORE_IO_TASK)
    @Override
    public void updateCount(String cacheKey) {
        try (MyLock lock = LockUtil.lock(cacheKey, CommonConst.COMMON_LOCK_LEASE_SECONDS)) {
            COUNT_STILL_NOT_CONSUMED.remove(cacheKey);
            RMap<String, String> rMap = RedisUtil.createMap(cacheKey);
            Map<String, String> allMap = rMap.readAllMap();
            if (MapUtil.isNotEmpty(allMap)) {
                long last = Long.parseLong(allMap.get(UpdateCount.LAST_UPDATE_KEY));
                long count = Long.parseLong(allMap.get(UpdateCount.COUNT_KEY));
                if (count > last) {
                    int splitIndex = cacheKey.lastIndexOf(Separator.COLON);
                    String prefix = cacheKey.substring(0, splitIndex + 1);
                    long id = Long.parseLong(cacheKey.substring(splitIndex + 1));
                    if (doUpdate(prefix, id, last, count)) {
                        RedisUtil.createMap(cacheKey).putIfExists(UpdateCount.LAST_UPDATE_KEY, count);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean doUpdate(String prefix, long id, long last, long count) {
        switch (prefix) {
            case CacheKey.COMMENT_COMMENTS_PREFIX:
                return commentService.updateComments(id, last, count);
            case CacheKey.POST_COMMENTS_PREFIX:
                return postService.updateComments(id, last, count);
            default:
                return false;
        }
    }
}
