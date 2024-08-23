package com.doro.orm.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.orm.base.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 用户的赞与踩
 *
 * @author jiage
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("doro_bbs_user_like")
public class UserLikeBean extends BaseBean {

    /**
     * 用户
     */
    private Long userId;

    /**
     * 帖子
     */
    private Long postId;

    /**
     * true：赞
     * false：踩
     */
    private Boolean like;
}
