package com.doro.orm.bean;


import com.baomidou.mybatisplus.annotation.*;
import com.doro.common.base.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 基础实体
 *
 * @author jiage
 */
@Setter
@Getter
public class BaseBean extends BaseModel {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Boolean del;
}
