package com.doro.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.bean.base.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

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
    private Long userId;

    /**
     * 帖子
     */
    private Long postId;

    /**
     * 内容
     */
    private String content;

    /**
     * 子评论数
     * 是否应该将该字段放在这里，而不是单独创建一个表
     */
    private Long comments;

    /**
     * 子评论列表
     */
    @TableField(exist = false)
    private List<SubCommentBean> subList;
}
