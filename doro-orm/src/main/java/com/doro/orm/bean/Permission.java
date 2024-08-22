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
@TableName("permission")
public class Permission extends BaseBean {

    /**
     * 权限
     */
    private String permission;

}
