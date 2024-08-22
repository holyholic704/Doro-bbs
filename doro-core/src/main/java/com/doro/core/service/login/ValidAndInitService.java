package com.doro.core.service.login;

import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;
import com.doro.core.valid.login.AbstractLoginValid;
import com.doro.core.valid.login.EmailLoginValid;
import com.doro.core.valid.login.PasswordLoginValid;
import com.doro.core.valid.login.PhoneLoginValid;
import org.springframework.stereotype.Component;

/**
 * 参数校验
 *
 * @author jiage
 */
@Component
public class ValidAndInitService {

    private final AbstractLoginValid first = new PhoneLoginValid();
    private final AbstractLoginValid second = new EmailLoginValid();
    private final AbstractLoginValid third = new PasswordLoginValid();

    /**
     * 检验参数
     *
     * @param requestUser 请求信息
     * @return 校验通过返回认证信息
     */
    public MyAuthenticationToken validAndInit(RequestUser requestUser) {
        return first.and(second)
                .and(third)
                .valid(requestUser);
    }
}
