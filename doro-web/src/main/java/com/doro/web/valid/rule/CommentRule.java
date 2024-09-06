package com.doro.web.valid.rule;

import com.doro.api.model.request.RequestComment;

/**
 * @author jiage
 */
public class CommentRule {

    private static final int MIN_PARAMS = 1;

    private static String validCurrentAndSize(RequestComment requestComment) {
        return requestComment.getCurrent() > -1 && requestComment.getSize() > 0 ? null : "请传入页码";
    }

    private static String validPostIdOrParentId(RequestComment requestComment) {
        return requestComment.getPostId() != null || requestComment.getParentId() != null ? null : "请传入帖子ID";
    }
}
