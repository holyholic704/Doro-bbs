package com.doro.core.service.login.provider;

import cn.hutool.core.util.StrUtil;
import com.doro.common.constant.Settings;
import com.doro.common.enumeration.LoginTypeEnum;
import lombok.Getter;

@Getter
public class Test {

    private LoginTypeEnum loginType;
    private boolean usePassword;

    private Test() {

    }

    public LoginTypeEnum initByLoginType(String loginType) {
        if (StrUtil.isNotEmpty(loginType)) {
            switch (loginType) {

            }
        }
        if (LoginTypeEnum.USE_PASSWORD.getType().equals(loginType)) {

        }
        if (Settings.LOGIN_WITH_EMAIL ) {
        }
        if (supportLoginType) {
            if (loginTypeEnum.getType().equals(loginType)) {
                return loginTypeEnum;
            }
        }
        return loginTypeEnum;
    }

    private LoginTypeEnum checkSupportLoginType(boolean supportLoginType, LoginTypeEnum loginTypeEnum, String loginType) {
        if (supportLoginType) {
            if (loginTypeEnum.getType().equals(loginType)) {
                return loginTypeEnum;
            }
        }
        return loginTypeEnum;
    }
}
