package com.doro.api.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.common.base.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 版块
 *
 * @author jiage
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("doro_bbs_section")
public class SectionBean extends BaseBean {

    /**
     * 名称
     */
    private String name;

    /**
     * 父版块
     */
    private Long parentId;

    /**
     * 创建人
     */
    private Long createUserId;

}
