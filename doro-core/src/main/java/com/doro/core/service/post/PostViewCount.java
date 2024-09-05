package com.doro.core.service.post;

import com.doro.api.orm.PostService;
import com.doro.common.constant.CacheKey;
import com.doro.core.service.count.BaseCountService;
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
}
