package com.doro.orm.model.request;

import com.doro.orm.base.BaseRequest;
import lombok.Getter;

/**
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
