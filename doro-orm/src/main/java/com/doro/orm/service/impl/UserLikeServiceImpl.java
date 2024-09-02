package com.doro.orm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.api.orm.UserLikeService;
import com.doro.bean.UserLikeBean;
import com.doro.orm.mapper.UserLikeMapper;
import org.springframework.stereotype.Service;

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

}
