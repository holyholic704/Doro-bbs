package com.doro.orm.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 发帖
 *
 * @author jiage
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("post")
public class Post extends BaseBean {

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

    /**
     * 文章类型 ID
     * TODO 是否需要？
     */
    private Long typeId;

    /**
     * 版块 ID
     */
    private Long sectionId;

    /**
     * 是否有效
     */
    private Boolean activated;

}
