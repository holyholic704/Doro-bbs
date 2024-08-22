package com.doro.core.exception;

/**
 * 参数校验异常
 *
 * @author jiage
 */
public class ValidException extends RuntimeException {

    public ValidException(String message) {
        super(message);
    }
}
