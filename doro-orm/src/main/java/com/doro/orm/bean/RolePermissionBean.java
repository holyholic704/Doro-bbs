package com.doro.orm.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.orm.base.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 角色权限
 *
 * @author jiage
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("doro_bbs_role_permission")
public class RolePermissionBean extends BaseBean {

    /**
     * 角色
     */
    private Long roleId;

    /**
     * 权限
     */
    private Long permissionId;

}

