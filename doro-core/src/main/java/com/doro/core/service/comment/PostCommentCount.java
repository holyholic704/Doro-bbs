package com.doro.core.service.comment;

import com.doro.api.orm.PostService;
import com.doro.common.constant.CacheKey;
import com.doro.core.service.count.BaseCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiage
 */
@Service(CacheKey.POST_COMMENTS_PREFIX)
public class PostCommentCount extends BaseCountService {

    private final PostService postService;

    @Autowired
    public PostCommentCount(PostService postService) {
        this.postService = postService;
    }

    @Override
    protected Long getFromDatabaseNullable(long id) {
        return postService.getPostComments(id);
    }
}
