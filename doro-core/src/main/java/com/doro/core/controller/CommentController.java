package com.doro.core.controller;

import com.doro.common.response.ResponseResult;
import com.doro.core.service.CoreCommentService;
import com.doro.orm.model.request.RequestComment;
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
        return coreCommentService.save(requestComment);
    }
}
