package com.doro.orm.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.doro.common.base.BaseModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 基础实体
 * 使用自增主键
 *
 * @author jiage
 */
@Setter
@Getter
public class BaseAutoIdBean extends BaseModel {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
}
