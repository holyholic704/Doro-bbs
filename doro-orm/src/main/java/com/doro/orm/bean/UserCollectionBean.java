package com.doro.orm.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.orm.base.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 用户的收藏
 *
 * @author jiage
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("doro_bbs_user_collection")
public class UserCollectionBean extends BaseBean {

    /**
     * 用户
     */
    private Long userId;

    /**
     * 帖子
     */
    private Long postId;
}
