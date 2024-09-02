package com.doro.api.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.common.base.BaseBean;
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
     * 点赞/踩对象类型
     */
    private Short type;

    /**
     * 点赞/踩对象
     */
    private Long objId;

    /**
     * true：赞
     * false：踩
     */
    private Boolean positive;
}
