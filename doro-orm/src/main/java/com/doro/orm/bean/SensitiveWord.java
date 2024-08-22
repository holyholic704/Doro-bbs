package com.doro.orm.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author jiage
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("sensitive_word")
public class SensitiveWord {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String word;
}
