package com.doro.core.service.comment;

import com.doro.api.orm.CommentService;
import com.doro.common.constant.CacheKey;
import com.doro.core.service.count.BaseCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiage
 */
@Service(CacheKey.COMMENT_COMMENTS_PREFIX)
public class CommentSubCount extends BaseCountService {

    private final CommentService commentService;

    @Autowired
    public CommentSubCount(CommentService commentService) {
        this.commentService = commentService;
    }

    @Override
    protected Long getFromDatabaseNullable(long id) {
        return commentService.getComments(id);
    }
}
