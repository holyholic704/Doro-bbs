package com.doro.common.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

/**
 * @author jiage
 */
@Setter
@Getter
public class Page<T> {

    private List<T> records = Collections.emptyList();

    private long total = 0;

    private long size = 10;

    private long current = 1;

    public Page(int current, int size) {
        this.current = current;
        this.size = size;
    }
}
