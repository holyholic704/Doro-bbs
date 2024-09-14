package com.doro.common.constant;

import java.time.Duration;

/**
 * @author jiage
 */
public class CommonConst {

    public static final Duration COMMON_CACHE_DURATION = Duration.ofSeconds(512);

    public static final Duration COMMON_HALF_CACHE_DURATION = Duration.ofSeconds(256);

    public static final long COMMON_LOCK_LEASE_SECONDS = 8;

    public static final long COMMON_DELAY_SECONDS = 5;

}
