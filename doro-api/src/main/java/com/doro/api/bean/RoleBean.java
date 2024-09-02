package com.doro.api.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.common.base.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 角色
 *
 * @author jiage
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("doro_bbs_role")
public class RoleBean extends BaseBean {

    /**
     * 角色
     */
    private String role;

}
