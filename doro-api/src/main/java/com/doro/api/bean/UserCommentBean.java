package com.doro.api.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 用户对于评论的赞和踩
 *
 * @author jiage
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("doro_bbs_user_comment_like")
public class UserCommentBean {

    /**
     * 用户
     */
    private Long userId;

    /**
     * 评论
     */
    private Long commentId;

    /**
     * true：赞
     * false：踩
     */
    private Boolean like;
}
