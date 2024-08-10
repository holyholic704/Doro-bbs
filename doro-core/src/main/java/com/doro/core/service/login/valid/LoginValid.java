package com.doro.core.service.login.valid;

import com.doro.core.model.request.RequestUser;
import com.doro.core.service.login.provider.MyAuthenticationToken;

@FunctionalInterface
public interface LoginValid<T, R> {

    R test(T t);

    default LoginValid<T, R> and(LoginValid<T, R> other) {
        return (t) -> {
            R result = test(t);
            return result != null ? result : other.test(t);
        };
    }

    default MyAuthenticationToken initAuthenticationToken(RequestUser requestUser, String loginType, boolean usePassword) {
        MyAuthenticationToken authenticationToken = new MyAuthenticationToken(requestUser.getUsername(), requestUser.getPassword());
        authenticationToken.setLoginType(loginType);
        authenticationToken.setUsePassword(usePassword);
        return authenticationToken;
    }
}
