package com.doro.core.service.count;


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

    private RSet<String> set = RedisUtil.createSet("woc");

    @Autowired
    public UpdateCountMqServiceImpl(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @Async("coreTask")
    public boolean updateCount(String key) {
        if (RedisUtil.isExists(key)) {
            RedisUtil.createSet(CacheKey.COUNT_STILL_NOT_CONSUMED).remove(key);
            try (MyLock lock = LockUtil.lock(key, CommonConst.COMMON_LOCK_LEASE_SECONDS)) {
                if (!update(key)) {
                    return update(key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private boolean update(String key) {
        RMap<String, String> rMap = RedisUtil.createMap(key);
        Map<String, String> allMap = rMap.readAllMap();
        if (allMap != null) {
            long last = Long.parseLong(allMap.get(UpdateCount.LAST_UPDATE_KEY));
            long count = Long.parseLong(allMap.get(UpdateCount.COUNT_KEY));
            if (count > last) {
                int splitIndex = key.lastIndexOf(Separator.COLON);
                String prefix = key.substring(0, splitIndex + 1);
                long id = Long.parseLong(key.substring(splitIndex + 1));

                switch (prefix) {
                    case CacheKey.COMMENT_COMMENTS_PREFIX:
                        return commentService.updateComments(id, last, count);
//                    case CacheKey.POST_COMMENTS_PREFIX:
//                        return postService.(id, expect, newValue);
                    default:
                        return true;
                }
            }
        }
        return true;
    }
}
