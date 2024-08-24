package com.doro.orm.model.request;

import com.doro.orm.base.BaseRequest;
import lombok.Getter;

/**
 * @author jiage
 */
@Getter
public class RequestPost extends BaseRequest {

    /**
     * 标题
     */
    private String title;

    /**
     * 作者
     */
    private Long authorId;

    /**
     * 正文
     */
    private String content;

    /**
     * 版块
     */
    private Long sectionId;

    /**
     * 标签列表
     */
    private String tagIds;

}
