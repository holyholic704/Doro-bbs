package com.doro.core.controller;

import com.doro.orm.request.RequestPost;
import com.doro.common.response.ResponseResult;
import com.doro.core.service.CorePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章管理
 *
 * @author jiage
 */
@RestController
@RequestMapping("post")
public class PostController {

    private final CorePostService corePostService;

    @Autowired
    public PostController(CorePostService corePostService) {
        this.corePostService = corePostService;
    }

    @PostMapping("save")
    public ResponseResult<?> save(@RequestBody RequestPost requestPost) {
        return corePostService.save(requestPost);
    }

    @PostMapping("getById")
    public ResponseResult<?> getById(@RequestParam Long postId) {
        return corePostService.getById(postId);
    }
}
