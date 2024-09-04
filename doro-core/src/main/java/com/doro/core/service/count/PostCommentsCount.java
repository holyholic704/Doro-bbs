package com.doro.core.service.count;

import com.doro.api.orm.PostService;
import com.doro.common.constant.CacheKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiage
 */
@Service(CacheKey.POST_COMMENTS_PREFIX)
public class PostCommentsCount extends BaseCountService {

    private final PostService postService;

    @Autowired
    public PostCommentsCount(PostService postService) {
        this.postService = postService;
    }

    @Override
    protected long getCountFromDatabase(long id) {
        return postService.getPostComments(id);
    }
}
