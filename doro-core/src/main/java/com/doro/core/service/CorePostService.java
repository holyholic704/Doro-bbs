package com.doro.core.service;

import cn.hutool.core.util.StrUtil;
import com.doro.common.response.ResponseResult;
import com.doro.core.exception.ValidException;
import com.doro.core.utils.UserUtil;
import com.doro.orm.bean.PostBean;
import com.doro.orm.request.RequestPost;
import com.doro.orm.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 帖子服务
 *
 * @author jiage
 */
@Service
public class CorePostService {

    @Autowired
    private PostService postService;

    public ResponseResult<?> savePost(RequestPost requestPost) {
        valid(requestPost);
        // 可以认为一定能获取到用户 ID
        Long authorId = UserUtil.getUserId();
        PostBean postBean = new PostBean()
                .setTitle(requestPost.getTitle())
                .setText(requestPost.getText())
                .setAuthorId(authorId)
                .setSectionId(requestPost.getSectionId())
                .setActivated(false);
        // TODO 发帖权限，等级
        // TODO 是否需要审核，审核规则？不审核、手动审核、敏感词过滤，谁来审核，版主？管理员？坛主？
        // TODO 字数限制
        // TODO 添加缓存，字数限制
        return postService.savePost(postBean) ? ResponseResult.success("保存成功") : ResponseResult.error("保存失败");
    }

    private void valid(RequestPost requestPost) {
        // TODO 版块校验规则，标题的最大、最小长度，正文的最大、最小长度
        if (StrUtil.isEmpty(requestPost.getTitle())) {
            throw new ValidException("请输入标题");
        }

        if (StrUtil.isEmpty(requestPost.getText())) {
            throw new ValidException("请输入正文");
        }

        // TODO 参数化
        if (requestPost.getText().length() > 10000) {
            throw new ValidException("文本长度过长");
        }
    }

}
