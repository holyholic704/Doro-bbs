package com.doro.core.model.request;

import com.doro.api.bean.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RequestUser extends User {

    /**
     * 验证码
     */
    private String code;

    /**
     * 登录方式
     */
    private String loginType;
}
