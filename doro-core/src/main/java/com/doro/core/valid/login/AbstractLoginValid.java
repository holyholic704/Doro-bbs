package com.doro.core.valid.login;

import cn.hutool.core.util.ReUtil;
import com.doro.common.constant.Regex;
import com.doro.core.exception.ValidException;
import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;
import com.doro.common.api.Valid;

/**
 * 登录注册校验
 *
 * @author jiage
 */
public abstract class AbstractLoginValid implements Valid<RequestUser, MyAuthenticationToken> {

    /**
     * 是否是支持的登录方式
     */
    protected void isSupportLoginType(boolean isSupport) {
        if (!isSupport) {
            throw new ValidException("不支持的登录方式");
        }
    }

    /**
     * 邮箱校验
     */
    protected void validEmail(String email) {
        if (!ReUtil.isMatch(Regex.EMAIL, email)) {
            throw new ValidException("邮箱格式错误");
        }
    }

    /**
     * 手机号校验
     */
    protected void validPhone(String phone) {
        if (!ReUtil.isMatch(Regex.PHONE, phone)) {
            throw new ValidException("手机号码格式错误");
        }
    }

    /**
     * 用户名校验
     */
    protected void validUsername(String username) {
        if (!ReUtil.isMatch(Regex.USERNAME, username)) {
            throw new ValidException("用户名格式错误");
        }
    }

    /**
     * 密码校验
     */
    protected void validPassword(String password) {
        if (!ReUtil.isMatch(Regex.PASSWORD, password)) {
            throw new ValidException("密码格式不正确");
        }
    }

    /**
     * 初始化认证信息
     *
     * @param requestUser 请求信息
     * @param loginType   登录方式
     * @param usePassword 是否使用密码
     * @return 认证信息
     */
    protected MyAuthenticationToken initAuthenticationToken(RequestUser requestUser, String loginType, boolean usePassword) {
        MyAuthenticationToken authenticationToken = new MyAuthenticationToken(requestUser.getUsername(), requestUser.getPassword());
        authenticationToken.setLoginType(loginType);
        authenticationToken.setUsePassword(usePassword);
        return authenticationToken;
    }
}
