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
     * 回复
     */
    private Long replyId;

    /**
     * 内容
     */
    private String content;
}
