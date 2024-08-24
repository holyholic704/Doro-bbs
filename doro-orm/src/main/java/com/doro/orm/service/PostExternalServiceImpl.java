package com.doro.orm.service;

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
public class PostExternalServiceImpl extends ServiceImpl<PostExternalMapper, PostExternalBean> implements PostExternalService {

}
