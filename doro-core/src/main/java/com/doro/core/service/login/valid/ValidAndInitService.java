package com.doro.core.service.login.valid;

import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class ValidAndInitService {

    private final AbstractValidAndInitChainHandler first = new UsernameValidChainHandler();

    {
        AbstractValidAndInitChainHandler second = new PhoneValidAndInitChainHandler();
        AbstractValidAndInitChainHandler third = new EmailValidAndInitChainHandler();
        AbstractValidAndInitChainHandler forth = new PasswordValidAndInitChainHandler();
        first.setNext(second);
        second.setNext(third);
        third.setNext(forth);
    }

    /**
     * 检验参数
     *
     * @param requestUser 请求信息
     * @return 校验通过返回 AuthenticationToken
     */
    public MyAuthenticationToken validAndInit(RequestUser requestUser) {
        return first.process(requestUser);
    }
}
