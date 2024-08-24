package com.doro.orm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.orm.api.PostExternalService;
import com.doro.orm.bean.PostExternalBean;
import com.doro.orm.mapper.PostExternalMapper;
import org.springframework.stereotype.Service;

/**
 * 帖子相关
 *
 * @author jiage
 */
@Service
class PostExternalServiceImpl extends ServiceImpl<PostExternalMapper, PostExternalBean> implements PostExternalService {

    @Override
    public Integer getPostViews(Long postId) {
        PostExternalBean postExternalBean = this.getOne(new LambdaQueryWrapper<PostExternalBean>()
                .eq(PostExternalBean::getPostId, postId));
        return postExternalBean != null ? postExternalBean.getView() : 0;
    }
}
