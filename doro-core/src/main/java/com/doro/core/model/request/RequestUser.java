package com.doro.core.model.request;

import com.doro.bean.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jiage
 */
@Getter
@Setter
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
