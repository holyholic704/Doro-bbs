package com.doro.core.service.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doro.api.bean.user.User;
import com.doro.core.mapper.user.UserMapper;
import org.springframework.stereotype.Service;

/**
 * 用户服务
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {
}
