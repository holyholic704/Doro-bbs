package com.doro.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.bean.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 全局配置
 *
 * @author jiage
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("doro_bbs_global_setting")
public class GlobalSettingBean extends BaseModel {

    /**
     * 字段
     */
    private String k;

    /**
     * 值
     */
    private String v;
}
