package com.doro.orm.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.orm.base.BaseAutoIdBean;
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
@TableName("global_setting")
public class GlobalSettingBean extends BaseAutoIdBean {

    /**
     * 字段
     */
    private String k;

    /**
     * 值
     */
    private String v;
}
