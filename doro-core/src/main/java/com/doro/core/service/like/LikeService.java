package com.doro.core.service.like;

import cn.hutool.core.collection.CollUtil;
import com.doro.api.model.request.RequestUserLike;
import com.doro.api.orm.LikeIndexService;
import com.doro.api.orm.UserLikeService;
import com.doro.bean.CommentBean;
import com.doro.bean.UserLikeBean;
import com.doro.cache.utils.RedisUtil;
import com.doro.common.constant.CacheKey;
import com.doro.common.constant.CommonConst;
import com.doro.common.constant.Separator;
import com.doro.common.enumeration.LikeType;
import com.doro.common.exception.ValidException;
import com.doro.core.utils.UserUtil;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jiage
 */
@Service
public class LikeService {

    private final LikeIndexService likeIndexService;
    private final UserLikeService userLikeService;

    @Autowired
    public LikeService(LikeIndexService likeIndexService, UserLikeService userLikeService) {
        this.likeIndexService = likeIndexService;
        this.userLikeService = userLikeService;
    }

    Map<Boolean, Byte> map = new HashMap<>(3);

    {
        map.put(true, (byte) 1);
        map.put(false, (byte) -1);
        map.put(null, (byte) 0);
    }

    /**
     * 赞或踩
     * 属于频繁更新的操作，且会存在重复请求的可能，虽然可以做防抖处理，但过于粗鲁，所以应先在缓存中更新，再定时写入到数据库中
     * 使用二级缓存？当前的读写使用本地缓存，持久化时使用远程缓存
     *
     * @param requestUserLike 请求信息
     * @return 赞或踩是否成功
     */
    public boolean like(RequestUserLike requestUserLike) {
        valid(requestUserLike);
        Long userId = UserUtil.getUserId();
        Short type = requestUserLike.getType();
        Long objId = requestUserLike.getObjId();

        String cacheKey = CacheKey.USER_LIKE_PREFIX + userId + Separator.COLON + type + Separator.COLON + objId;
        RBucket<Byte> bucket = RedisUtil.createBucket(cacheKey);
        bucket.set(map.get(requestUserLike.getPositive()), CommonConst.COMMON_CACHE_DURATION);
        System.out.println(bucket.get());

//        RMap<Long, String> rMap = RedisUtil.createMap(CacheKey.USER_LIKE_PREFIX + userId + Separator.COLON + type);
//        rMap.put(objId, String.valueOf(map.get(requestUserLike.getPositive())));
//        System.out.println(Byte.valueOf(rMap.get(objId)));

        UserLikeBean userLikeBean = new UserLikeBean()
                .setUserId(userId)
                .setPositive(requestUserLike.getPositive())
                .setType(type);
        userLikeBean.setId(objId);
//        return userLikeService.like(userLikeBean);
        return true;
    }

    public Boolean getUserPostLike(Long userId, Long postId) {
        return userLikeService.getUserLike(userId, postId);
    }

    public Map<Long, Boolean> getUserCommentLikes(Long userId, List<CommentBean> commentList) {
        if (CollUtil.isNotEmpty(commentList)) {
            List<Long> commentIds = commentList.stream().map(CommentBean::getId).collect(Collectors.toList());
            userLikeService.getUserLikes(userId, commentIds);
        }
        return null;
    }

    private void valid(RequestUserLike requestUserLike) {
        if (requestUserLike.getObjId() == null) {
            throw new ValidException("没有赞/踩的对象");
        }

        if (LikeType.getLikeType(requestUserLike.getType()) == null) {
            throw new ValidException("没有赞/踩的对象");
        }
//
//        if (requestUserLike.getPositive() == null) {
//            throw new ValidException("没有赞/踩的对象");
//        }
    }

}
