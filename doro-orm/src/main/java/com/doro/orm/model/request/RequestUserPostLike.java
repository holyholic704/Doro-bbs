package com.doro.orm.model.request;

import com.doro.orm.base.BaseRequest;
import lombok.Getter;

/**
 * @author jiage
 */
@Getter
public class RequestUserPostLike extends BaseRequest {

    /**
     * 帖子
     */
    private Long postId;

    /**
     * true：赞
     * false：踩
     */
    private Boolean positive;
}
