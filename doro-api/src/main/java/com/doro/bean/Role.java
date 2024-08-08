package com.doro.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.bean.base.BaseBean;
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
public class Role extends BaseBean {

    /**
     * 角色
     */
    private String role;

}
