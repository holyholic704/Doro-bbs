package com.doro.bean.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 基础模型
 * 所有实体类都要直接或间接继承该类
 *
 * @author jiage
 */
@Setter
@Getter
public class BaseModel implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
}
