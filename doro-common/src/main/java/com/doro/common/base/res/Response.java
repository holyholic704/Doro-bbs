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
public class Response<T> implements Serializable {

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
    private static final Cache<ResponseEnum, Response> ENUM_RESPONSE_CACHE = Caffeine.newBuilder()
            .softValues()
            .maximumSize(20)
            .build();

    private static final ReentrantLock LOCK = new ReentrantLock(true);

    /**
     * 不允许外部创建
     */
    private Response() {
    }

    private Response(T data) {
        this.code = ResponseEnum.SUCCESS.getCode();
        this.message = ResponseEnum.SUCCESS.getMessage();
        this.data = data;
    }

    private Response(String message) {
        this.code = ResponseEnum.ERROR.getCode();
        this.message = message;
    }

    private Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private Response(ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

    public static <T> Response<T> success() {
        return Response.ofEnum(ResponseEnum.SUCCESS);
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(data);
    }

    public static <T> Response<T> error() {
        return Response.ofEnum(ResponseEnum.ERROR);
    }

    public static <T> Response<T> error(String message) {
        return new Response<>(message);
    }

    /**
     * 使用枚举创建
     *
     * @param responseEnum 枚举
     * @return 响应对象
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> Response<T> ofEnum(ResponseEnum responseEnum) {
        Response response = ENUM_RESPONSE_CACHE.getIfPresent(responseEnum);
        if (response == null) {
            try {
                LOCK.lock();
                // 二次校验
                if ((response = ENUM_RESPONSE_CACHE.getIfPresent(responseEnum)) != null) {
                    return response;
                }
                response = new Response(responseEnum);
                ENUM_RESPONSE_CACHE.put(responseEnum, response);
            } finally {
                LOCK.unlock();
            }
        }
        return response;
    }
}
