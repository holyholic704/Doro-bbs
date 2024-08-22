package com.doro.orm.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.common.base.BaseBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author jiage
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class Section extends BaseBean {

    private String name;
}
