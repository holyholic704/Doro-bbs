package com.doro.core.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.bean.Post;
import com.doro.core.model.request.RequestPost;
import com.doro.core.utils.UserUtil;
import com.doro.orm.mapper.PostMapper;

/**
 * 文章服务
 *
 * @author jiage
 */
public class PostService extends ServiceImpl<PostMapper, Post> {

    public boolean savePost(RequestPost requestPost) {
        // 可以认为一定能获取到用户 ID
        Long authorId = UserUtil.getUserId();
        // TODO 发帖权限，等级
        // TODO 是否需要审核，审核规则？不审核、手动审核、敏感词过滤，谁来审核，版主？管理员？坛主？
        // TODO 字数限制
        return this.save(requestPost);
    }


    public boolean savePost(Post post) {
        return this.save(post);
    }
}
