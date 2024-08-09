package com.doro.core.service.login.valid;

import cn.hutool.core.util.StrUtil;
import com.doro.core.exception.MyAuthenticationException;
import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;

public class UsernameValidChainHandler extends AbstractValidAndInitChainHandler {

    @Override
    protected MyAuthenticationToken handle(RequestUser requestUser) {
        if (StrUtil.isEmpty(requestUser.getUsername())) {
            throw new MyAuthenticationException("用户名为空");
        }
        return doNextHandler(requestUser);
    }
}
