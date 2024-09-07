package com.doro.core.service.count;

import com.doro.api.orm.PostService;
import com.doro.common.constant.CacheKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiage
 */
@Service(CacheKey.POST_VIEWS_PREFIX)
public class PostViewCount extends BaseCountService {

    private final PostService postService;

    @Autowired
    public PostViewCount(PostService postService) {
        this.postService = postService;
    }

    @Override
    public Long getCountFromDatabase(long id) {
        return postService.getPostViews(id);
    }

    @Override
    protected void updateDatabaseCount(long id, Long count) {
        if (count != null) {
            postService.updateViews(id, count);
        }
    }
}
