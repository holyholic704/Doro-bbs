package com.doro.orm.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 评论
 *
 * @author jiage
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("comment")
public class Comment extends BaseBean {

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
