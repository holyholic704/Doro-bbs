package com.doro.orm.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.orm.base.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 用户角色
 *
 * @author jiage
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("user_role")
public class UserRoleBean extends BaseBean {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;
}
