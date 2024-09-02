package com.doro.api.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.common.base.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 标签
 *
 * @author jiage
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("doro_bbs_tag")
public class TagBean extends BaseBean {

    /**
     * 标签名
     */
    private String name;
}
