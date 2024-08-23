package com.doro.core.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.orm.bean.Comment;
import com.doro.orm.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
 * 评论
 *
 * @author jiage
 */
@Service
public class CommentService extends ServiceImpl<CommentMapper, Comment> {

    public boolean saveComment(Comment comment) {
        return this.save(comment);
    }
}
