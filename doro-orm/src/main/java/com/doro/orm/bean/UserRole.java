package com.doro.orm.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.common.base.BaseBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_role")
public class UserRole extends BaseBean {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;
}
