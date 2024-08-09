package com.doro.core.config;

import com.doro.res.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ResponseStatus(HttpStatus.OK)
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseResult<String> handleRuntimeException(Exception e) {
//
//        return ResponseResult.error();
//    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseResult<String> handleAuthenticationException(Exception e) {
        e.printStackTrace();
        return ResponseResult.error(e.getMessage());
    }
}
