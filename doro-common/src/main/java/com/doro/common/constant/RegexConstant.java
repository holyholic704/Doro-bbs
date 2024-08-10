package com.doro.common.constant;

import cn.hutool.core.lang.RegexPool;

public class RegexConstant {

    /**
     * 用户名校验
     * 可以是数字、字母，且长度在 6 ~ 16
     */
    public static final String USERNAME = "^[a-zA-Z0-9]{6,16}$";

    /**
     * 手机号校验
     */
    public static final String PHONE = "^1[3-9]\\d{9}$";

    /**
     * 邮箱校验
     */
    public static final String EMAIL = RegexPool.EMAIL_WITH_CHINESE;

    /**
     * 密码校验
     * 至少包括字母、数字、特殊符号（_*@!#%?$~·^&-+=,.）中的两种，且长度在 8 ~ 16
     */
    public static final String PASSWORD = "^(?![a-zA-Z]+$)(?!\\d+$)(?![_*@!#%?$~·^&-+=,.]+$)[\\dA-Za-z_*@!#%?$~·^&-+=,.]{6,16}$";
}
