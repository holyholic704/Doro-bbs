package com.doro.web.controller;

import com.doro.api.model.request.RequestUserLike;
import com.doro.common.enumeration.MessageEnum;
import com.doro.common.response.ResponseResult;
import com.doro.core.service.like.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 赞与踩
 *
 * @author jiage
 */
@RestController
@RequestMapping("like")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("like")
    public ResponseResult<?> like(@RequestBody RequestUserLike requestUserLike) {
        return likeService.like(requestUserLike) ? ResponseResult.success(MessageEnum.SAVE_SUCCESS) : ResponseResult.error(MessageEnum.SAVE_ERROR);
    }
}
