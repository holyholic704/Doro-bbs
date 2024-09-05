package com.doro.api.common;

/**
 * @author jiage
 */
public interface CountService {

    Long getCount(long id);

    Long getCountFromCache(long id);

    Long getCountFromDatabase(long id);

    void incrCount(long id);

    void decrCount(long id);

    void correctCount(long id);
}
