package com.doro.api.common;

/**
 * 参数校验
 *
 * @param <T> 待校验的对象
 * @param <R> 返回结果
 * @author jiage
 */
public interface Valid<T, R> {

    /**
     * 校验
     *
     * @param t 待校验的对象
     * @return 返回结果
     */
    R valid(T t);

    /**
     * 责任链
     *
     * @param next 下一个校验方式
     * @return 返回结果
     */
    default Valid<T, R> and(Valid<T, R> next) {
        return (t) -> {
            R result = valid(t);
            return result != null ? result : next.valid(t);
        };
    }
}
