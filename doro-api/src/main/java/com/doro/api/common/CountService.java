package com.doro.api.common;

/**
 * @author jiage
 */
public interface CountService {

    long getCount(Long id);

    Long getCountFromCache(Long id);

    void incrCount(Long id, int add);

    void decrCount(Long id, int add);

    boolean delCache(Long id);
}
