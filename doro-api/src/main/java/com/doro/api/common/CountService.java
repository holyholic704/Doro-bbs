package com.doro.api.common;

/**
 * @author jiage
 */
public interface CountService {

    long getCount(long id);

    long getCountFromCache(long id);

    long getCountFromDatabase(long id);

    void incrCount(long id);

    void decrCount(long id);

    void correctCount(long id);
}
