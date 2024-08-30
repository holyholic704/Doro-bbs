package com.doro.orm.model.request;

import com.doro.orm.base.BaseRequest;
import lombok.Getter;

/**
 * 评论相关请求
 * <p>
 * 需要添加按照时间或热度排序吗，或者只按照时间先后排序，并在第一页显示一些热评
 *
 * @author jiage
 */
@Getter
public class RequestComment extends BaseRequest {

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

    /**
     * 内容
     */
    private String content;
}
