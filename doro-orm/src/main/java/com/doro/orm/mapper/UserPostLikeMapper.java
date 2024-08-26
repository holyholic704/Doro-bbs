package com.doro.orm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doro.orm.bean.UserPostLikeBean;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * 用户的赞与踩
 *
 * @author jiage
 */
public interface UserPostLikeMapper extends BaseMapper<UserPostLikeBean> {

    @MapKey("positive")
    @Select("SELECT positive, COUNT( 1 ) nums FROM doro_bbs_user_post_like WHERE post_id = #{postId} GROUP BY positive;")
    Map<Boolean, UserPostLikeBean> getPostLikes(Long postId);
}
