package com.doro.orm.model.request;

import com.doro.orm.base.BaseRequest;
import lombok.Getter;

/**
 * @author jiage
 */
@Getter
public class RequestSection extends BaseRequest {

    /**
     * 版块名称
     */
    private String name;

    /**
     * 父版块
     */
    private Long parentId;
}
