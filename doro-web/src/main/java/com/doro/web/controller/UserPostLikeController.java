package com.doro.web.controller;

import com.doro.common.enumeration.MessageEnum;
import com.doro.common.response.ResponseResult;
import com.doro.core.service.CoreUserLikeService;
import com.doro.api.model.request.RequestUserLike;
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

    private final CoreUserLikeService userPostLikeService;

    @Autowired
    public UserPostLikeController(CoreUserLikeService userPostLikeService) {
        this.userPostLikeService = userPostLikeService;
    }

    @PostMapping("like")
    public ResponseResult<?> like(@RequestBody RequestUserLike requestUserLike) {
        return userPostLikeService.like(requestUserLike) ? ResponseResult.success(MessageEnum.SAVE_SUCCESS) : ResponseResult.error(MessageEnum.SAVE_ERROR);
    }
}
