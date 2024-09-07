package com.doro.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.bean.base.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 子评论
 *
 * @author jiage
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("doro_bbs_sub_comment")
public class SubCommentBean extends BaseBean {

    /**
     * 评论人
     */
    private Long userId;

    /**
     * 内容
     */
    private String content;

    /**
     * 帖子
     */
    private Long postId;

    /**
     * 父评论
     */
    private Long parentId;

    /**
     * 被回复的评论
     */
    private Long repliedId;

    /**
     * 被回复人
     */
    private Long repliedUserId;
}
