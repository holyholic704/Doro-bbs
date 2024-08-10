package com.doro.core.service.login;

import cn.hutool.core.util.StrUtil;
import com.doro.bean.User;
import com.doro.common.constant.Settings;
import com.doro.core.model.request.RequestUser;
import com.doro.core.model.response.ResponseUser;
import com.doro.core.service.UserService;
import com.doro.core.service.login.provider.MyAuthenticationToken;
import com.doro.core.service.login.valid.ValidAndInitService;
import com.doro.core.utils.JwtUtil;
import com.doro.res.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 登录注册服务
 */
@Service
public class LoginService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ValidAndInitService validAndInitService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 登录
     *
     * @param requestUser 请求信息
     * @return 是否成功登录
     */
    public ResponseResult<?> login(RequestUser requestUser) {
        MyAuthenticationToken authenticationToken = validAndInitService.validAndInit(requestUser);

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
        if (StrUtil.isNotEmpty(requestUser.getUsername()) && StrUtil.isNotEmpty(requestUser.getPassword()) && userService.notExist(requestUser.getUsername())) {
            User register = new User()
                    .setUsername(requestUser.getUsername())
                    .setPassword(bCryptPasswordEncoder.encode(requestUser.getPassword()))
                    .setEnable(!Settings.USER_NEED_ACTIVE);

            if (userService.saveUser(register)) {
                if (Settings.USER_NEED_ACTIVE) {
                    registerNeedActive(register);
                    return ResponseResult.success("请等待激活");
                }
                MyAuthenticationToken authenticationToken = new MyAuthenticationToken(register.getUsername(), null, null);
                authenticationToken.setDetails(register.getId());
                // 将认证信息存储在 SecurityContextHolder 中
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                return ResponseResult.success(initToken(authenticationToken));
            }
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
//        MyAuthenticationToken authenticationToken = (MyAuthenticationToken) authentication.getPrincipal();
//        // 生成token
//        String token = jwtUtil.generate(authenticationToken);
//        String username = (String) authenticationToken.getPrincipal();
//        // 缓存token
//        redisTemplate.opsForValue().set(username, token, expired, TimeUnit.SECONDS);
//        return new ResponseUser(token);
        return null;
    }

    /**
     * 注册用户是否需要激活
     *
     * @param user 用户信息
     */
    private void registerNeedActive(User user) {
        // TODO 激活功能
    }
}
