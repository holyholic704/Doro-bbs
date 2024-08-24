package com.doro.orm.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.orm.bean.PostInfoBean;
import com.doro.orm.mapper.PostInfoMapper;
import org.springframework.stereotype.Service;

/**
 * 帖子相关
 *
 * @author jiage
 */
@Service
public class PostInfoService extends ServiceImpl<PostInfoMapper, PostInfoBean> {

}
