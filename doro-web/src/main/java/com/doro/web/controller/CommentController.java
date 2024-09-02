package com.doro.web.controller;

import com.doro.common.enumeration.MessageEnum;
import com.doro.common.model.Page;
import com.doro.common.response.ResponseResult;
import com.doro.core.service.comment.CoreCommentService;
import com.doro.bean.CommentBean;
import com.doro.api.model.request.RequestComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 评论管理
 *
 * @author jiage
 */
@RestController
@RequestMapping("comment")
public class CommentController {

    private final CoreCommentService coreCommentService;

    @Autowired
    public CommentController(CoreCommentService coreCommentService) {
        this.coreCommentService = coreCommentService;
    }

    @PostMapping("save")
    public ResponseResult<?> save(@RequestBody RequestComment requestComment) {
        return coreCommentService.save(requestComment) ? ResponseResult.success(MessageEnum.SAVE_SUCCESS) : ResponseResult.error(MessageEnum.SAVE_ERROR);
    }

    @PostMapping("page")
    public ResponseResult<?> page(@RequestBody RequestComment requestComment) {
        Page<CommentBean> page = coreCommentService.page(requestComment);
        return page != null && page.getTotal() > 0 ? ResponseResult.success(page) : ResponseResult.error(MessageEnum.NO_DATA_ERROR);
    }
}
