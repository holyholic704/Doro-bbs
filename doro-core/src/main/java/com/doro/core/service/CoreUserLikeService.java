package com.doro.core.service;

import com.doro.common.exception.ValidException;
import com.doro.core.utils.UserUtil;
import com.doro.api.orm.UserLikeService;
import com.doro.api.bean.UserLikeBean;
import com.doro.api.model.request.RequestUserLike;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户的赞与踩
 *
 * @author jiage
 */
@Service
public class CoreUserLikeService {

    private final UserLikeService userLikeService;
    private final RedissonClient redissonClient;

    @Autowired
    public CoreUserLikeService(UserLikeService userLikeService, RedissonClient redissonClient) {
        this.userLikeService = userLikeService;
        this.redissonClient = redissonClient;
    }

    /**
     * TODO 缓存？异步？
     */
    public boolean like(RequestUserLike requestUserLike) {
        valid(requestUserLike);
        Long userId = UserUtil.getUserId();
        String key = "USER_LIKE" + userId;
        redissonClient.getMapCache(key);
        UserLikeBean userLikeBean = new UserLikeBean()
                .setUserId(UserUtil.getUserId())
                .setPositive(requestUserLike.getPositive())
                .setType(requestUserLike.getType())
                .setObjId(requestUserLike.getObjId());
        return userLikeService.like(userLikeBean);
    }

    private void valid(RequestUserLike requestUserLike) {
        if (requestUserLike.getObjId() == null) {
            throw new ValidException("没有关联的帖子");
        }
    }
}
