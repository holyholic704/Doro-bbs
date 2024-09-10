package com.doro.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.bean.base.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 赞与踩的汇总
 *
 * @author jiage
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("doro_bbs_like_index")
public class LikeIndexBean extends BaseBean {

    /**
     * 点赞/踩对象类型
     */
    private Short type;

    /**
     * 点赞/踩对象所属用户
     */
    private Long memberId;

    /**
     * 赞的数量
     */
    private Long positiveNum;

    /**
     * 踩的数量
     */
    private Long negativeNum;
}
