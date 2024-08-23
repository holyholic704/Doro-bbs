package com.doro.orm.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doro.orm.base.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

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
     * 正文
     */
    private String text;

//    /**
//     * 文章类型 ID
//     * TODO 是否需要？
//     */
//    private Long typeId;

    /**
     * 版块 ID
     */
    private Long sectionId;

    /**
     * 是否有效
     */
    private Boolean activated;

}
