package com.doro.core.model.request;

import com.doro.common.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jiage
 */
@Getter
@Setter
public class RequestPost extends BaseRequest {

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
     * 版块 ID
     */
    private Long sectionId;

}
