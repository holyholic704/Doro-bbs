package com.doro.api.model.request;

import com.doro.api.model.request.base.BaseRequest;
import lombok.Getter;

/**
 * @author jiage
 */
@Getter
public class RequestUserLike extends BaseRequest {

    /**
     * 点赞/踩对象类型
     */
    private Short type;

    /**
     * 点赞/踩对象
     */
    private Long objId;

    /**
     * true：赞
     * false：踩
     */
    private Boolean positive;
}
