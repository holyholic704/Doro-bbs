package com.doro.orm.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.orm.api.CommentService;
import com.doro.orm.bean.CommentBean;
import com.doro.orm.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
 * 评论
 *
 * @author jiage
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentBean> implements CommentService {

    @Override
    public boolean saveComment(CommentBean commentBean) {
        return this.save(commentBean);
    }
}
