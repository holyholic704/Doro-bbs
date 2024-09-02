package com.doro.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.bean.base.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 用户的关注
 *
 * @author jiage
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("doro_bbs_user_collection")
public class UserFollowBean extends BaseBean {

    /**
     * 用户
     */
    private Long userId;

    /**
     * 关注的用户
     */
    private Long followId;
}
