package com.doro.core.model.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jiage
 */
@Getter
@Setter
public class BaseRequest {

    private int current;

    private int size;

    protected Page<?> asPage() {
        return new Page<>(current, size);
    }
}
