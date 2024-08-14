package com.doro.core.utils;

import cn.hutool.core.util.ReUtil;
import com.doro.common.constant.RegexConstant;
import com.doro.core.exception.MyAuthenticationException;

/**
 * 登录校验工具包
 */
public class LoginValidUtil {

    /**
     * 是否是支持的登录方式
     */
    public static void isSupportLoginType(boolean isSupport) {
        if (!isSupport) {
            throw new MyAuthenticationException("不支持的登录方式");
        }
    }

    /**
     * 邮箱校验
     */
    public static void validEmail(String email) {
        if (!ReUtil.isMatch(RegexConstant.EMAIL, email)) {
            throw new MyAuthenticationException("邮箱格式错误");
        }
    }

    /**
     * 手机号校验
     */
    public static void validPhone(String phone) {
        if (!ReUtil.isMatch(RegexConstant.PHONE, phone)) {
            throw new MyAuthenticationException("手机号码格式错误");
        }
    }

    /**
     * 用户名校验
     */
    public static void validUsername(String username) {
        if (!ReUtil.isMatch(RegexConstant.USERNAME, username)) {
            throw new MyAuthenticationException("用户名格式错误");
        }
    }

    /**
     * 密码校验
     */
    public static void validPassword(String password) {
        if (!ReUtil.isMatch(RegexConstant.PASSWORD, password)) {
            throw new MyAuthenticationException("密码格式不正确");
        }
    }

    /**
     * 用户不存在
     */
    public static void userNotExist(boolean exist) {
        if (!exist) {
            throw new MyAuthenticationException("用户不存在");
        }
    }

    /**
     * 用户已存在
     */
    public static void userExisted(boolean exist) {
        if (exist) {
            throw new MyAuthenticationException("该用户已存在");
        }
    }
}
