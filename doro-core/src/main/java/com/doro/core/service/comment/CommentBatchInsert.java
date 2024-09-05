package com.doro.core.service.comment;

import com.doro.api.orm.CommentService;
import com.doro.bean.CommentBean;
import com.doro.common.constant.CacheKey;
import com.doro.core.service.count.BaseCountService;
import com.github.yitter.idgen.YitIdHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author jiage
 */
@Service
public class CommentBatchInsert {

    private final CommentService commentService;
    private final BaseCountService postCommentCount;

    @Autowired
    public CommentBatchInsert(CommentService commentService, @Qualifier(CacheKey.POST_COMMENTS_PREFIX) BaseCountService postCommentCount) {
        this.commentService = commentService;
        this.postCommentCount = postCommentCount;
//        test();
    }

    private BlockingQueue<CommentBean> queue = new ArrayBlockingQueue<>(1024);

    public void addTask(CommentBean bean) {
        queue.add(bean);
        if (queue.size() > 896) {
            saveBatch(896);
        }
    }

    private final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(5);

    public void test() {
        executor.scheduleAtFixedRate(() -> {
            if (!queue.isEmpty()) {
                saveBatch(100);
            }
        }, 1, 5, TimeUnit.SECONDS);
    }

    private void saveBatch(int size) {
        List<CommentBean> currentList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (queue.isEmpty()) {
                break;
            }
            CommentBean bean = queue.poll();
            bean.setId(YitIdHelper.nextId());
            currentList.add(bean);
        }

        if (currentList.size() > 0) {
            if (doUpdate(currentList)) {
                postCommentCount.updateCount(currentList.get(0).getPostId(), currentList.size());
            } else {
                // TODO MQ 消息
            }
        }
    }

    private boolean doUpdate(List<CommentBean> currentList) {
        try {
            return commentService.saveBatchComment(currentList);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
