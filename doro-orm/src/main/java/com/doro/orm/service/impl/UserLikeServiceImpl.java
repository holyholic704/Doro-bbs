package com.doro.orm.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.api.orm.UserLikeService;
import com.doro.bean.UserLikeBean;
import com.doro.orm.mapper.UserLikeMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户的赞与踩
 *
 * @author jiage
 */
@Service
class UserLikeServiceImpl extends ServiceImpl<UserLikeMapper, UserLikeBean> implements UserLikeService {

    @Override
    public boolean like(UserLikeBean userLikeBean) {
        return this.save(userLikeBean);
    }

    @Override
    public Boolean getUserLike(Long userId, Long objId) {
        UserLikeBean userLikeBean = this.getOne(new LambdaQueryWrapper<UserLikeBean>()
                .eq(UserLikeBean::getUserId, userId)
                .eq(UserLikeBean::getObjId, objId));
        return userLikeBean == null ? null : userLikeBean.getPositive();
    }

    @Override
    public Map<Long, Boolean> getUserLikes(Long userId, List<Long> objIds) {
        List<UserLikeBean> userLikeBeanList = this.list(new LambdaQueryWrapper<UserLikeBean>()
                .eq(UserLikeBean::getUserId, userId)
                .in(UserLikeBean::getObjId, objIds));

        if (CollUtil.isNotEmpty(userLikeBeanList)) {
            return userLikeBeanList.stream().collect(Collectors.toMap(UserLikeBean::getObjId, UserLikeBean::getPositive));
        }

        return null;
    }
}
