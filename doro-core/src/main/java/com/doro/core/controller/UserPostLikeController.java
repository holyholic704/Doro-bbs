package com.doro.core.controller;

import com.doro.common.response.ResponseResult;
import com.doro.core.service.CoreUserPostLikeService;
import com.doro.orm.model.request.RequestUserPostLike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户的赞与踩管理
 *
 * @author jiage
 */
@RestController
@RequestMapping("like")
public class UserPostLikeController {

    private final CoreUserPostLikeService userPostLikeService;

    @Autowired
    public UserPostLikeController(CoreUserPostLikeService userPostLikeService) {
        this.userPostLikeService = userPostLikeService;
    }

    @PostMapping("like")
    public ResponseResult<?> like(@RequestBody RequestUserPostLike requestUserPostLike) {
        return userPostLikeService.like(requestUserPostLike);
    }
}
