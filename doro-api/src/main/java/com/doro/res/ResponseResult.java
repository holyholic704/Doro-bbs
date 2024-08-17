package com.doro.res;

import com.doro.common.enumeration.ResponseEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 统一响应
 */
@Getter
@Setter
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

    private ResponseResult(T data) {
        this.code = ResponseEnum.SUCCESS.getCode();
        this.message = ResponseEnum.SUCCESS.getMessage();
        this.data = data;
    }

    private ResponseResult(String message) {
        this.code = ResponseEnum.ERROR.getCode();
        this.message = message;
    }

    private ResponseResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private ResponseResult(ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

    public static <T> ResponseResult<T> success() {
        return ResponseResult.ofEnum(ResponseEnum.SUCCESS);
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(data);
    }

    public static <T> ResponseResult<T> error() {
        return ResponseResult.ofEnum(ResponseEnum.ERROR);
    }

    public static <T> ResponseResult<T> error(String message) {
//        return LocalCacheUtil.computeLocalIfAbsent(CacheConstant.ENUM_RESPONSE_CACHE,
//                message,
//                new ResponseResult<>(message));
        return null;
    }

    /**
     * 使用枚举创建
     *
     * @param responseEnum 枚举
     * @return 响应对象
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> ResponseResult<T> ofEnum(ResponseEnum responseEnum) {
//        return LocalCacheUtil.computeLocalIfAbsent(CacheConstant.ENUM_RESPONSE_CACHE,
//                responseEnum.getMessage(),
//                new ResponseResult(responseEnum));
        return null;
    }
}
