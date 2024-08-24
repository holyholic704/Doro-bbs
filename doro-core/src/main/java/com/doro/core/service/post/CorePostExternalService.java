package com.doro.core.service.post;

import com.doro.cache.api.MyLock;
import com.doro.cache.utils.LockUtil;
import com.doro.orm.api.PostExternalService;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiage
 */
@Service
public class CorePostExternalService {

    private final PostExternalService postExternalService;
    private final RedissonClient redissonClient;

    @Autowired
    public CorePostExternalService(PostExternalService postExternalService, RedissonClient redissonClient) {
        this.postExternalService = postExternalService;
        this.redissonClient = redissonClient;
    }

//    public Integer incrAndGetViews(Long postId) {
//        redissonClient.createBatch().
//    }

}
