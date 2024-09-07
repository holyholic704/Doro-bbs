package com.doro.core.service.comment;

import cn.hutool.json.JSONUtil;
import com.doro.api.orm.CommentService;
import com.doro.bean.CommentBean;
import com.doro.common.constant.CacheKey;
import com.doro.core.service.count.BaseCountService;
import com.doro.mq.producer.CommentSaveProducer;
import com.github.yitter.idgen.YitIdHelper;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CommentBatchInsert {

    private final CommentService commentService;
    private final BaseCountService postCommentCount;
    private final CommentSaveProducer commentSaveProducer;

    @Autowired
    public CommentBatchInsert(CommentService commentService, @Qualifier(CacheKey.POST_COMMENTS_PREFIX) BaseCountService postCommentCount, CommentSaveProducer commentSaveProducer) {
        this.commentService = commentService;
        this.postCommentCount = postCommentCount;
        this.commentSaveProducer = commentSaveProducer;
        test();
    }

    private final BlockingQueue<CommentBean> queue = new ArrayBlockingQueue<>(1024);

    public boolean addTask(CommentBean bean) {
        bean.setId(YitIdHelper.nextId());

        boolean addSuccess = queue.offer(bean);
        boolean sendSuccess = commentSaveProducer.send(JSONUtil.toJsonStr(bean), 10);

        if (queue.size() > 256) {
            log.info("执行一次");
            saveBatch(256);
        }

        return addSuccess && sendSuccess;
    }

    private final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(5);

    private void test() {
        executor.scheduleAtFixedRate(() -> {
            if (!queue.isEmpty()) {
                saveBatch(50);
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
            currentList.add(bean);
        }

        if (currentList.size() > 0) {
            log.info("长度" + currentList.size());
            doUpdate(currentList);
//            if (doUpdate(currentList)) {
//                postCommentCount.updateCount(currentList.get(0).getPostId(), currentList.size());
//            } else {
//                // TODO MQ 消息
//            }
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
