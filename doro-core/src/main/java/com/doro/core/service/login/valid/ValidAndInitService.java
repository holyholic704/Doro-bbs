package com.doro.core.service.login.valid;

import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class ValidAndInitService {

    private final LoginValid<RequestUser, MyAuthenticationToken> first = new UsernameValid();
    private final LoginValid<RequestUser, MyAuthenticationToken> second = new PhoneLoginValid();
    private final LoginValid<RequestUser, MyAuthenticationToken> third = new EmailLoginValid();
    private final LoginValid<RequestUser, MyAuthenticationToken> forth = new PasswordValid();

    /**
     * 检验参数
     *
     * @param requestUser 请求信息
     * @return 校验通过返回认证信息
     */
    public MyAuthenticationToken validAndInit(RequestUser requestUser) {
        return first.and(second)
                .and(third)
                .and(forth)
                .test(requestUser);
    }
}
