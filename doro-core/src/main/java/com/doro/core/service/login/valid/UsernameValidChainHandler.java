package com.doro.core.service.login.valid;

import cn.hutool.core.util.StrUtil;
import com.doro.core.exception.MyAuthenticationException;
import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;

/**
 * 用户名校验
 */
public class UsernameValidChainHandler extends AbstractValidAndInitChainHandler {

    /**
     * 校验逻辑
     */
    @Override
    protected MyAuthenticationToken handle(RequestUser requestUser) {
        validUsername(requestUser.getUsername());
        return null;
    }

    /**
     * 用户名校验
     *
     * @param username 用户名
     */
    private void validUsername(String username) {
        if (StrUtil.isEmpty(username)) {
            throw new MyAuthenticationException("用户名格式错误");
        }
    }
}
