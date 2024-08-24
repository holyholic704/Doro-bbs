package com.doro.orm.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.orm.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 帖子相关
 *
 * @author jiage
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("doro_bbs_post_external")
public class PostExternalBean extends BaseModel {

    /**
     * 帖子
     */
    private Long postId;

    /**
     * 浏览
     */
    private Integer view;

    /**
     * 分享
     * TODO 后续实现
     */
    private Long share;
}
