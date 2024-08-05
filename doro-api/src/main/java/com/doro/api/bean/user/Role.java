package com.doro.api.bean.user;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.common.base.BaseModel;
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
@TableName("role")
public class Role extends BaseModel {

    /**
     * 角色
     */
    private String role;

}
