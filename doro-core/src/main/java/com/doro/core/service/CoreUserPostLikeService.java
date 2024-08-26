package com.doro.core.service;

import com.doro.common.enumeration.MessageEnum;
import com.doro.common.exception.ValidException;
import com.doro.common.response.ResponseResult;
import com.doro.core.utils.UserUtil;
import com.doro.orm.api.UserPostLikeService;
import com.doro.orm.bean.UserPostLikeBean;
import com.doro.orm.model.request.RequestUserPostLike;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户的赞与踩
 *
 * @author jiage
 */
@Service
public class CoreUserPostLikeService {

    private final UserPostLikeService userPostLikeService;
    private final RedissonClient redissonClient;

    @Autowired
    public CoreUserPostLikeService(UserPostLikeService userPostLikeService, RedissonClient redissonClient) {
        this.userPostLikeService = userPostLikeService;
        this.redissonClient = redissonClient;
    }

    /**
     * TODO 缓存？异步？
     */
    public ResponseResult<?> like(RequestUserPostLike requestUserPostLike) {
        valid(requestUserPostLike);
        Long userId = UserUtil.getUserId();
        String key = "USER_LIKE" + userId;
        redissonClient.getMapCache(key);
        UserPostLikeBean userPostLikeBean = new UserPostLikeBean()
                .setUserId(UserUtil.getUserId())
                .setPositive(requestUserPostLike.getPositive())
                .setPostId(requestUserPostLike.getPostId());
        return userPostLikeService.like(userPostLikeBean) ? ResponseResult.success(MessageEnum.SAVE_SUCCESS) : ResponseResult.error(MessageEnum.SAVE_ERROR);
    }

    private void valid(RequestUserPostLike requestUserPostLike) {
        if (requestUserPostLike.getPostId() == null) {
            throw new ValidException("没有关联的帖子");
        }
    }
}
