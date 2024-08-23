package com.doro.core.controller;

import com.doro.core.model.request.RequestPost;
import com.doro.common.response.ResponseResult;
import com.doro.core.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文章管理
 *
 * @author jiage
 */
@RestController("post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("save")
    public ResponseResult<?> savePost(@RequestBody RequestPost requestPost) {
        return postService.savePost(requestPost);
    }
}
