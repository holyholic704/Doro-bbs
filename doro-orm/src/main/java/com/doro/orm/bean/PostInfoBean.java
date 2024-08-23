package com.doro.orm.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.orm.base.BaseAutoIdBean;
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
@TableName("post_info")
public class PostInfoBean extends BaseAutoIdBean {

    /**
     * 帖子
     */
    private Long postId;

    /**
     * 浏览
     */
    private Long view;

    /**
     * 点赞
     */
    private Long like;

    /**
     * 点踩
     * TODO 后续实现
     */
    private Long dislike;

    /**
     * 收藏
     */
    private Long collection;

    /**
     * 分享
     * TODO 后续实现
     */
    private Long share;
}
