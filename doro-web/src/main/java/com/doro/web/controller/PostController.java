package com.doro.web.controller;

import com.doro.common.enumeration.MessageEnum;
import com.doro.common.response.ResponseResult;
import com.doro.core.service.post.CorePostService;
import com.doro.bean.PostBean;
import com.doro.api.model.request.RequestPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 帖子管理
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
        return corePostService.save(requestPost) ? ResponseResult.success(MessageEnum.SAVE_SUCCESS) : ResponseResult.error(MessageEnum.SAVE_ERROR);
    }

    @PostMapping("getById")
    public ResponseResult<?> getById(@RequestParam Long postId) {
        PostBean postBean = corePostService.getById(postId);
        return postBean != null ? ResponseResult.success(postBean) : ResponseResult.error(MessageEnum.NO_DATA_ERROR);
    }
}
