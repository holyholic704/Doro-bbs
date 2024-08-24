package com.doro.common.response;

import com.doro.common.enumeration.MessageEnum;
import com.doro.common.enumeration.ResponseEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 统一响应
 *
 * @author jiage
 */
@Getter
@Setter(AccessLevel.PRIVATE)
public class ResponseResult<T> implements Serializable {

    /**
     * 状态码
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 不允许外部创建
     */
    private ResponseResult() {
    }

    private ResponseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private ResponseResult(int code, String message) {
        this(code, message, null);
    }

    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage());
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(
                ResponseEnum.SUCCESS.getCode(),
                ResponseEnum.SUCCESS.getMessage(),
                data);
    }

    public static ResponseResult<String> success(MessageEnum messageEnum) {
        return new ResponseResult<>(
                ResponseEnum.SUCCESS.getCode(),
                ResponseEnum.SUCCESS.getMessage(),
                messageEnum.getMessage());
    }

    public static <T> ResponseResult<T> error(String message) {
        return new ResponseResult<>(ResponseEnum.ERROR.getCode(), message);
    }

    public static <T> ResponseResult<T> error() {
        return ResponseResult.error(ResponseEnum.ERROR.getMessage());
    }

    public static <T> ResponseResult<T> error(MessageEnum messageEnum) {
        return ResponseResult.error(messageEnum.getMessage());
    }
}
