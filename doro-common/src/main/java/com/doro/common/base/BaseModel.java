package com.doro.common.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseModel implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

}
