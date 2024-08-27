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
 * 帖子
 *
 * @author jiage
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("doro_bbs_post")
public class PostBean extends BaseBean {

    /**
     * 标题
     */
    private String title;

    /**
     * 作者 ID
     */
    private Long authorId;

    /**
     * 作者
     */
    private String authorName;

    /**
     * 正文
     */
    private String content;

//    /**
//     * 帖子类型
//     * TODO 是否需要？
//     */
//    private Long typeId;

    /**
     * 版块 ID
     */
    private Long sectionId;

    /**
     * 版块
     */
    private String sectionName;

    /**
     * 是否有效
     */
    private Boolean activated;

    /**
     * 浏览量
     */
    private Long views;

    /**
     * 点赞
     */
    @TableField(exist = false)
    private Integer likes;

    /**
     * 喜欢
     */
    @TableField(exist = false)
    private List<CommentBean> comments;
}
