package com.doro.core.service.login.valid;

import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;

/**
 * 登录参数验证
 */
public abstract class AbstractValidAndInitChainHandler {

    private AbstractValidAndInitChainHandler nextHandler;

    /**
     * 添加下一个处理流程
     */
    public void setNext(AbstractValidAndInitChainHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * 处理流程
     *
     * @param requestUser 请求信息
     * @return 认证信息
     */
    protected MyAuthenticationToken process(RequestUser requestUser) {
        MyAuthenticationToken authenticationToken = handle(requestUser);
        if (authenticationToken != null) {
            return authenticationToken;
        }
        if (this.nextHandler != null) {
            return this.nextHandler.process(requestUser);
        }
        return null;
    }

    /**
     * 校验逻辑
     */
    protected abstract MyAuthenticationToken handle(RequestUser requestUser);

    /**
     * 初始化认证信息
     *
     * @param requestUser 请求信息
     * @param loginType   登录方式
     * @param usePassword 对应的登录方式是否需要使用密码
     * @return 认证信息
     */
    protected MyAuthenticationToken initAuthenticationToken(RequestUser requestUser, String loginType, boolean usePassword) {
        MyAuthenticationToken authenticationToken = new MyAuthenticationToken(requestUser.getUsername(), requestUser.getPassword());
        authenticationToken.setLoginType(loginType);
        authenticationToken.setUsePassword(usePassword);
        return authenticationToken;
    }
}
