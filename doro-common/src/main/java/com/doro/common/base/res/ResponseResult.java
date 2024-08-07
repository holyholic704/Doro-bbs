package com.doro.common.base.res;

import com.doro.common.enumeration.ResponseEnum;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.concurrent.locks.ReentrantLock;

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
     * 缓存使用枚举创建的对象，避免过多的重复创建
     */
    @SuppressWarnings("rawtypes")
    private static final Cache<ResponseEnum, ResponseResult> ENUM_RESPONSE_CACHE = Caffeine.newBuilder()
            .softValues()
            .maximumSize(20)
            .build();

    private static final ReentrantLock LOCK = new ReentrantLock(true);

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
        return new ResponseResult<>(message);
    }

    /**
     * 使用枚举创建
     *
     * @param responseEnum 枚举
     * @return 响应对象
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> ResponseResult<T> ofEnum(ResponseEnum responseEnum) {
        ResponseResult responseResult = ENUM_RESPONSE_CACHE.getIfPresent(responseEnum);
        if (responseResult == null) {
            try {
                LOCK.lock();
                // 二次校验
                if ((responseResult = ENUM_RESPONSE_CACHE.getIfPresent(responseEnum)) != null) {
                    return responseResult;
                }
                responseResult = new ResponseResult(responseEnum);
                ENUM_RESPONSE_CACHE.put(responseEnum, responseResult);
            } finally {
                LOCK.unlock();
            }
        }
        return responseResult;
    }
}
