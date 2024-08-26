package com.doro.orm.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.orm.api.UserPostLikeService;
import com.doro.orm.bean.UserPostLikeBean;
import com.doro.orm.mapper.UserPostLikeMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用户的赞与踩
 *
 * @author jiage
 */
@Service
class UserPostLikeServiceImpl extends ServiceImpl<UserPostLikeMapper, UserPostLikeBean> implements UserPostLikeService {


    @Override
    public boolean like(UserPostLikeBean userPostLikeBean) {
        return this.save(userPostLikeBean);
    }

    @Override
    public Map<Boolean, UserPostLikeBean> getPostLikes(Long postId) {
        return this.getBaseMapper().getPostLikes(postId);
    }


}
