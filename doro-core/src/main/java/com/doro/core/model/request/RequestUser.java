package com.doro.core.model.request;

import com.doro.common.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jiage
 */
@Getter
@Setter
public class RequestUser extends BaseRequest {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 登录方式
     */
    private String loginType;
}
