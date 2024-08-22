package com.doro.core.service.login;

import com.doro.bean.User;
import com.doro.core.exception.ValidException;
import com.doro.core.model.request.RequestUser;
import com.doro.core.model.response.ResponseUser;
import com.doro.core.service.UserService;
import com.doro.core.service.login.provider.MyAuthenticationToken;
import com.doro.core.service.setting.G_Setting;
import com.doro.core.service.setting.GlobalSettingAcquire;
import com.doro.core.utils.JwtUtil;
import com.doro.res.ResponseResult;
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
    private final ValidAndInitService validAndInitService;
    private final UserService userService;

    @Autowired
    public LoginService(BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, ValidAndInitService validAndInitService, UserService userService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.validAndInitService = validAndInitService;
        this.userService = userService;
    }

    /**
     * 登录
     *
     * @param requestUser 请求信息
     * @return 是否成功登录
     */
    public ResponseResult<?> login(RequestUser requestUser) {
        // 参数校验、生成认证信息
        Authentication authenticationToken = validAndInitService.validAndInit(requestUser);
        // 认证，失败抛出异常
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        ResponseUser responseUser = initToken(authentication);
        return responseUser != null ? ResponseResult.success(responseUser) : ResponseResult.error("登录失败");
    }

    /**
     * 注册
     *
     * @param requestUser 请求信息
     * @return 是否注册成功
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<?> register(RequestUser requestUser) {
        MyAuthenticationToken authenticationToken = validAndInitService.validAndInit(requestUser);
        // 检查是否有重复的用户
        userExisted(userService.existUser((String) authenticationToken.getPrincipal(), authenticationToken.getLoginType()));

        boolean userNeedActive = GlobalSettingAcquire.get(G_Setting.USER_NEED_ACTIVE);

        User register = new User()
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
     * @param user 用户信息
     */
    private void registerNeedActive(User user) {
        // TODO 激活功能
    }

    private void userExisted(boolean exist) {
        if (exist) {
            throw new ValidException("该用户已存在");
        }
    }
}
