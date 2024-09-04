package com.doro.core.service.comment;

import com.doro.api.dto.CountSnapshot;
import com.doro.api.orm.CommentService;
import com.doro.api.orm.PostService;
import com.doro.cache.utils.RedisUtil;
import com.doro.common.constant.CacheKey;
import com.doro.common.constant.CommonConst;
import com.doro.common.enumeration.UpdateCount;
import org.redisson.api.RMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 帖子评论量及父评论的评论量
 *
 * @author jiage
 */
@Service
public class CommentCountService {

    private final CommentService commentService;
    private final PostService postService;

    @Autowired
    public CommentCountService(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    public void updateComments(String cachePrefix, Long id) {
        updateComments(cachePrefix, id, 1);
    }

    public void decrComments(String cachePrefix, Long id) {
        updateComments(cachePrefix, id, -1);
    }

    private CountSnapshot updateComments(String cachePrefix, Long id, int add) {
        String cacheKey = cachePrefix + id;

        List<?> responses = RedisUtil.initBatch(cacheKey)
                .expire(CommonConst.COMMON_CACHE_DURATION)
                .isExists()
                .execute();

        boolean isExist = (Boolean) responses.get(1);

        if (!isExist) {
            long comments;
            if (CacheKey.POST_COMMENTS_PREFIX.equals(cachePrefix)) {
                comments = postService.getPostComments(id);
            } else {
                comments = commentService.getComments(id);
            }

            boolean isSuccess = initCache(cacheKey, comments, comments + add);
            if (isSuccess) {
                return new CountSnapshot(CacheKey.POST_COMMENTS_PREFIX,
                        id,
                        comments,
                        comments + add);
            }
        }

        responses = RedisUtil.initBatch(cacheKey)
                .mapGet(UpdateCount.LAST_UPDATE_KEY)
                .mapAddAndGet(UpdateCount.COUNT_KEY, add)
                .execute();
        long last = Long.parseLong(String.valueOf(responses.get(0)));
        long count = Long.parseLong(String.valueOf(responses.get(1)));
        return new CountSnapshot(CacheKey.POST_COMMENTS_PREFIX,
                id,
                last,
                count);
    }

    public long getCommentCount(Long id) {
        String cacheKey = CacheKey.POST_COMMENTS_PREFIX + id;

        List<?> result = RedisUtil.initBatch(cacheKey)
                .expire(CommonConst.COMMON_CACHE_DURATION)
                .isExists()
                .execute();

        boolean isExist = (Boolean) result.get(1);

        if (isExist) {
            RMap<String, String> rMap = RedisUtil.createMap(cacheKey);
            return Long.parseLong(rMap.get(UpdateCount.COUNT_KEY));
        } else {
            // 不需要判断是否为 0，可能该帖子就是没有评论
            long comments = postService.getPostComments(id);
            initCache(cacheKey, comments, comments);
            return comments;
        }
    }

    private boolean initCache(String cacheKey, long last, long count) {
        List<?> result = RedisUtil.initBatch(cacheKey)
                .mapPutIfAbsent(UpdateCount.LAST_UPDATE_KEY, last)
                .mapPutIfAbsent(UpdateCount.COUNT_KEY, count)
                .expire(CommonConst.COMMON_CACHE_DURATION)
                .execute();
        return result.get(0) == null;
    }

//    protected long getPostCommentsFromCache(Long id) {
//        return getCommentsFromCache(CacheKey.POST_COMMENTS_PREFIX + id);
//    }
//
//    protected long getSubCommentsFromCache(Long id) {
//        return getCommentsFromCache(CacheKey.COMMENT_COMMENTS_PREFIX + id);
//    }
//
//    private long getCommentsFromCache(String cacheKey) {
//        RMap<String, Long> rMap = RedisUtil.createMap(cacheKey);
//        return rMap.get(CommentConst.COMMENTS_COUNTS);
//    }
}
