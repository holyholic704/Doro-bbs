package com.doro.orm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.api.orm.LikeIndexService;
import com.doro.bean.LikeIndexBean;
import com.doro.orm.mapper.LikeIndexMapper;
import org.springframework.stereotype.Service;

/**
 * 赞与踩的汇总
 *
 * @author jiage
 */
@Service
class LikeIndexServiceImpl extends ServiceImpl<LikeIndexMapper, LikeIndexBean> implements LikeIndexService {
}
