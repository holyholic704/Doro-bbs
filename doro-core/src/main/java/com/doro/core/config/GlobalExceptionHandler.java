package com.doro.core.config;

import com.doro.common.enumeration.ResponseEnum;
import com.doro.core.exception.SystemException;
import com.doro.core.exception.ValidException;
import com.doro.common.response.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    /**
     * 参数校验异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ValidException.class)
    public ResponseResult<String> handleMethodValidException(Exception e) {
        return ResponseResult.error(e.getMessage());
    }

    /**
     * 参数校验异常
     * Spring 的权限校验，灵活性不足，只少量使用
     */
    @SuppressWarnings("ConstantConditions")
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseResult.error(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * 系统异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(SystemException.class)
    public ResponseResult<String> handleSystemException(Exception e) {
        e.printStackTrace();
        return ResponseResult.ofEnum(ResponseEnum.SYSTEM_ERROR);
    }

    /**
     * 认证校验异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseResult<String> handleAuthenticationException(Exception e) {
        // TODO 记得去除
        e.printStackTrace();
        return ResponseResult.error(e.getMessage());
    }
}
