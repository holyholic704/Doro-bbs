package com.doro.core.service.post;

import cn.hutool.core.util.StrUtil;
import com.doro.common.constant.PostConst;
import com.doro.common.enumeration.MessageEnum;
import com.doro.common.exception.ValidException;
import com.doro.common.response.ResponseResult;
import com.doro.core.utils.UserUtil;
import com.doro.orm.api.PostService;
import com.doro.orm.api.SectionService;
import com.doro.orm.bean.PostBean;
import com.doro.orm.model.request.RequestPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 帖子服务
 *
 * @author jiage
 */
@Service
public class CorePostService {

    private final SectionService sectionService;
    private final PostService postService;

    @Autowired
    public CorePostService(SectionService sectionService, PostService postService) {
        this.sectionService = sectionService;
        this.postService = postService;
    }

    public ResponseResult<?> save(RequestPost requestPost) {
        valid(requestPost);
        // 可以认为一定能获取到用户 ID
        Long authorId = UserUtil.getUserId();
        PostBean postBean = new PostBean()
                .setTitle(requestPost.getTitle())
                .setContent(requestPost.getContent())
                .setAuthorId(authorId)
                .setSectionId(requestPost.getSectionId())
                .setActivated(false);
        // TODO 发帖权限，等级
        // TODO 是否需要审核，审核规则？不审核、手动审核、敏感词过滤，谁来审核，版主？管理员？坛主？
        // TODO 字数限制
        // TODO 添加缓存，字数限制
        // TODO 防灌水
        return postService.savePost(postBean) ? ResponseResult.success(MessageEnum.SAVE_SUCCESS) : ResponseResult.error(MessageEnum.SAVE_ERROR);
    }

    public ResponseResult<?> getById(Long postId) {
        // TODO 浏览量，在一定时间内多次获取同一帖子，不增加浏览量
        PostBean post = postService.getPostById(postId);
        return post != null ? ResponseResult.success(post) : ResponseResult.error(MessageEnum.NO_DATA_ERROR);
    }

    public ResponseResult<?> page(RequestPost requestPost) {
        return ResponseResult.success(postService.page(requestPost));
    }

    private void valid(RequestPost requestPost) {
        // TODO 帖子校验规则
        // TODO 标签暂时非必须
        if (StrUtil.isEmpty(requestPost.getTitle()) || requestPost.getTitle().length() < PostConst.MIN_TITLE_LENGTH || requestPost.getTitle().length() > PostConst.MAX_TITLE_LENGTH) {
            throw new ValidException(String.format("请输入标题（%s ~ %s 个字）", PostConst.MIN_TITLE_LENGTH, PostConst.MAX_TITLE_LENGTH));
        }

        if (requestPost.getContent() == null || requestPost.getContent().length() < PostConst.MIN_CONTENT_LENGTH || requestPost.getContent().length() > PostConst.MAX_CONTENT_LENGTH) {
            throw new ValidException(String.format("请输入正文（%s ~ %s 个字）", PostConst.MIN_CONTENT_LENGTH, PostConst.MAX_CONTENT_LENGTH));
        }

        if (requestPost.getSectionId() == null || !sectionService.hasSection(requestPost.getSectionId())) {
            throw new ValidException("请选择正确的版块");
        }
    }

}
