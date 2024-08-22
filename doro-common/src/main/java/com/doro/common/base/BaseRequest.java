package com.doro.common.base;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础请求模型
 * 所有请求对象都要继承该类
 *
 * @author jiage
 */
@Getter
@Setter
public class BaseRequest {

    /**
     * 当前页
     */
    private int current;

    /**
     * 数量
     */
    private int size;

}
