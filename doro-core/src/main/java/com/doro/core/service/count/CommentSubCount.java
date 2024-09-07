package com.doro.core.service.count;

import com.doro.api.orm.CommentService;
import com.doro.common.constant.CacheKey;
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
    public Long getCountFromDatabase(long id) {
        return commentService.getComments(id);
    }

    @Override
    protected boolean updateDatabaseCount(long id, long expect, long newValue) {
        return commentService.updateComments(id, expect, newValue);
    }
}
