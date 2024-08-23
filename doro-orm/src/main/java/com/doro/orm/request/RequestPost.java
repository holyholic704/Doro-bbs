package com.doro.orm.request;

import com.doro.orm.base.BaseRequest;
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
     * 作者
     */
    private Long authorId;

    /**
     * 正文
     */
    private String text;

    /**
     * 版块
     */
    private Long sectionId;

    /**
     * 标签列表
     */
    private String tagIds;

}
