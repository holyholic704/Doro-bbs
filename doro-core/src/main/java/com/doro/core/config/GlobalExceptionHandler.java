package com.doro.core.config;

import com.doro.common.enumeration.ResponseEnum;
import com.doro.core.exception.SystemException;
import com.doro.res.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author jiage
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(SystemException.class)
    public ResponseResult<String> handleSystemException(Exception e) {
        e.printStackTrace();
        return ResponseResult.ofEnum(ResponseEnum.SYSTEM_ERROR);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseResult<String> handleAuthenticationException(Exception e) {
        // TODO 记得去除
        e.printStackTrace();
        return ResponseResult.error(e.getMessage());
    }
}
