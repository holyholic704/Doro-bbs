package com.doro.core.service.comment;

import com.doro.cache.utils.RedisUtil;
import com.doro.common.constant.CacheKey;
import com.doro.common.constant.CommentConst;
import com.doro.api.orm.CommentService;
import com.doro.api.orm.PostService;
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
    private final MergeUpdate mergeUpdate;

    @Autowired
    public CommentCountService(CommentService commentService, PostService postService, MergeUpdate mergeUpdate) {
        this.commentService = commentService;
        this.postService = postService;
        this.mergeUpdate = mergeUpdate;
    }

    public long updateComments(String cachePrefix, Long id) {
        return updateComments(cachePrefix, id, 1);
    }

    public long decrComments(String cachePrefix, Long id) {
        return updateComments(cachePrefix, id, -1);
    }

    public long updateComments(String cachePrefix, Long id, int add) {
        String cacheKey = cachePrefix + id;

        List<?> responses = RedisUtil.initBatch(cacheKey)
                .expire(CommentConst.COMMENTS_CACHE)
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
                return comments + add;
            }
        }
        RMap<String, Integer> rMap = RedisUtil.createMap(cacheKey);
        return rMap.addAndGet(CommentConst.COMMENTS_COUNTS, add).longValue();
    }

    public long getCommentCount(Long id) {
        String cacheKey = CacheKey.POST_COMMENTS_PREFIX + id;

        List<?> result = RedisUtil.initBatch(cacheKey)
                .expire(CommentConst.COMMENTS_CACHE)
                .isExists()
                .execute();

        boolean isExist = (Boolean) result.get(1);

        if (isExist) {
            RMap<String, Integer> rMap = RedisUtil.createMap(cacheKey);
            return rMap.get(CommentConst.COMMENTS_COUNTS);
        } else {
            // 不需要判断是否为 0，可能该帖子就是没有评论
            long comments = postService.getPostComments(id);
            initCache(cacheKey, comments, comments);
            return comments;
        }
    }

    private boolean initCache(String cacheKey, long last, long count) {
        List<?> result = RedisUtil.initBatch(cacheKey)
                .putIfAbsent(CommentConst.COMMENTS_LAST_UPDATE, last)
                .putIfAbsent(CommentConst.COMMENTS_COUNTS, count)
                .expire(CommentConst.COMMENTS_CACHE)
                .execute();
        return result.get(0) == null;
    }

    protected long getPostCommentsFromCache(Long id) {
        return getCommentsFromCache(CacheKey.POST_COMMENTS_PREFIX + id);
    }

    protected long getSubCommentsFromCache(Long id) {
        return getCommentsFromCache(CacheKey.COMMENT_COMMENTS_PREFIX + id);
    }

    private long getCommentsFromCache(String cacheKey) {
        RMap<String, Long> rMap = RedisUtil.createMap(cacheKey);
        return rMap.get(CommentConst.COMMENTS_COUNTS);
    }
}
