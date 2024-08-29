package com.doro.orm.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.orm.base.BaseBean;
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
     */
    private Long comments;

    /**
     * 子评论列表
     */
    @TableField(exist = false)
    private List<CommentBean> subList;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CommentBean && getId() != null) {
            CommentBean compareBean = (CommentBean) obj;
            return getId().equals(compareBean.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.getId().hashCode();
    }
}
