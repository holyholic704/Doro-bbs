package com.doro.core.service.login.valid;

import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class ValidAndInitService {

    private final AbstractValidAndInitChainHandler first = new UsernameValidChainHandler();

    {
        AbstractValidAndInitChainHandler phoneValid = new PhoneValidAndInitChainHandler();
        AbstractValidAndInitChainHandler emailValid = new EmailValidAndInitChainHandler();
        AbstractValidAndInitChainHandler passwordValid = new PasswordValidAndInitChainHandler();
        first.setNext(phoneValid);
        phoneValid.setNext(emailValid);
        emailValid.setNext(passwordValid);
    }

    public MyAuthenticationToken validAndInit(RequestUser requestUser) {
        return first.process(requestUser);
    }
}
