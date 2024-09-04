package com.doro.api.common;

/**
 * @author jiage
 */
public interface CountService {

    long getCount(Long id);

    long getCountFromCache(Long id);

    long getCountFromDatabase(long id);

    void incrCount(Long id);

    void decrCount(Long id);
}
