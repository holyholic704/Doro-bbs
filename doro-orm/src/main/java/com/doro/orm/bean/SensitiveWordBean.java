package com.doro.orm.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.orm.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 敏感词
 *
 * @author jiage
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("doro_bbs_sensitive_word")
public class SensitiveWordBean extends BaseModel {

    /**
     * 敏感词
     */
    private String word;
}
