package com.doro.core.service.login;

import com.doro.common.constant.LoginConst;
import com.doro.common.exception.ValidException;
import com.doro.common.response.ResponseResult;
import com.doro.core.model.response.ResponseUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;
import com.doro.core.service.setting.G_Setting;
import com.doro.core.service.setting.GlobalSettingAcquire;
import com.doro.core.utils.JwtUtil;
import com.doro.core.valid.ValidService;
import com.doro.orm.api.UserService;
import com.doro.orm.bean.UserBean;
import com.doro.orm.model.request.RequestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 登录注册服务
 *
 * @author jiage
 */
@Service
public class LoginService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ValidService validService;
    private final UserService userService;

    @Autowired
    public LoginService(BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, ValidService validService, UserService userService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.validService = validService;
        this.userService = userService;
    }

    /**
     * 登录
     *
     * @param requestUser 请求信息
     * @return 是否成功登录
     */
    public ResponseUser login(RequestUser requestUser) {
        // 参数校验、生成认证信息
        Authentication authenticationToken = validService.validAndInit(requestUser);
        // 认证，失败抛出异常
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        return initToken(authentication);
    }

    /**
     * 注册
     *
     * @param requestUser 请求信息
     * @return 是否注册成功
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<?> register(RequestUser requestUser) {
        MyAuthenticationToken authenticationToken = validService.validAndInit(requestUser);
        // 检查是否有重复的用户
        isUserExisted((String) authenticationToken.getPrincipal(), authenticationToken.getLoginType());

        boolean userNeedActive = GlobalSettingAcquire.get(G_Setting.USER_NEED_ACTIVE);

        UserBean register = new UserBean()
                .setUsername(requestUser.getUsername())
                .setPassword(bCryptPasswordEncoder.encode(requestUser.getPassword()))
                .setEnable(!userNeedActive);

        if (userService.saveUser(register)) {
            if (userNeedActive) {
                registerNeedActive(register);
                return ResponseResult.success("请等待激活");
            }
            authenticationToken.setDetails(register.getId());
            // 将认证信息存储在 SecurityContextHolder 中
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            return ResponseResult.success(initToken(authenticationToken));
        }
        return ResponseResult.error("注册失败");
    }

    /**
     * 返回含有 Token 的响应信息
     *
     * @param authentication 认证信息
     * @return 响应信息
     */
    public ResponseUser initToken(Authentication authentication) {
        MyAuthenticationToken authenticationToken = (MyAuthenticationToken) authentication;
        return new ResponseUser((Long) authenticationToken.getDetails(),
                (String) authenticationToken.getPrincipal(),
                JwtUtil.generateAndCache((String) authenticationToken.getPrincipal(), (Long) authenticationToken.getDetails()));
    }

    /**
     * 注册用户是否需要激活
     *
     * @param userBean 用户信息
     */
    private void registerNeedActive(UserBean userBean) {
        // TODO 激活功能
    }

    /**
     * 该用户是否已存在
     *
     * @param username  用户名，可以为手机号、邮箱
     * @param loginType 登录方式，这里为查询方式
     */
    private void isUserExisted(String username, String loginType) {
        // 注意这里不要改为 swicth，后续 LoginConst 可能会改为变量
        boolean existed;
        if (LoginConst.USE_PHONE.equals(loginType)) {
            existed = userService.hasPhoneUser(username);
        } else if (LoginConst.USE_EMAIL.equals(loginType)) {
            existed = userService.hasEmailUser(username);
        } else {
            existed = userService.hasUser(username);
        }
        if (existed) {
            throw new ValidException("该用户已存在");
        }
    }
}
