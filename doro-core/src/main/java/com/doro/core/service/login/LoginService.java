package com.doro.core.service.login;

import cn.hutool.core.util.StrUtil;
import com.doro.common.model.res.ResponseResult;
import com.doro.common.constant.LoginConstant;
import com.doro.core.model.request.RequestUser;
import com.doro.core.model.response.ResponseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 登录注册服务
 */
@Service
public class LoginService {

    @Autowired
    private Map<String, BaseLoginService> loginServiceMap;

    /**
     * 登录
     *
     * @param requestUser 请求信息
     * @return 是否成功登录
     */
    public ResponseResult<?> login(RequestUser requestUser) {
        BaseLoginService baseLoginService;
        // 默认使用密码登录
        if (StrUtil.isEmpty(requestUser.getLoginType()) || (baseLoginService = loginServiceMap.get(requestUser.getLoginType())) == null) {
            baseLoginService = loginServiceMap.get(LoginConstant.USE_PASSWORD);
        }
        ResponseUser responseUser = baseLoginService.login(requestUser);
        return responseUser != null ? ResponseResult.success(responseUser) : ResponseResult.error("登录失败");
    }
}
