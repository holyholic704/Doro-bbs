package com.doro.orm.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.orm.base.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 评论
 *
 * @author jiage
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("doro_bbs_comment")
public class CommentBean extends BaseBean {

    /**
     * 评论人
     */
    private Long authorId;

    /**
     * 帖子
     */
    private Long postId;

    /**
     * 回复
     */
    private Long replyId;

    /**
     * 内容
     */
    private String content;
}
