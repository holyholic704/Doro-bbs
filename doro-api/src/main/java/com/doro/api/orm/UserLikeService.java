package com.doro.api.orm;

import com.doro.bean.UserLikeBean;

import java.util.List;
import java.util.Map;

/**
 * 用户的赞与踩
 *
 * @author jiage
 */
public interface UserLikeService {

    boolean like(UserLikeBean userLikeBean);

    Boolean getUserLike(Long userId, Long objId);

    Map<Long, Boolean> getUserLikes(Long userId, List<Long> objIds);
}
