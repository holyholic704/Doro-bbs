package com.doro.orm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doro.bean.UserLikeBean;

/**
 * 用户的赞与踩
 *
 * @author jiage
 */
public interface UserLikeMapper extends BaseMapper<UserLikeBean> {

//    @MapKey("positive")
//    @Select("SELECT positive, COUNT( 1 ) nums FROM doro_bbs_user_like WHERE post_id = #{postId} GROUP BY positive;")
//    Map<Boolean, UserLikeBean> getPostLikes(Long postId);
}
