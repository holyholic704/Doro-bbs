package com.doro.common.constant;

import java.time.Duration;

/**
 * 帖子相关常量
 *
 * @author jiage
 */
public class PostConst {

    public static final int MIN_TITLE_LENGTH = 3;

    public static final int MAX_TITLE_LENGTH = 30;

    public static final int MIN_CONTENT_LENGTH = 10;

    public static final int MAX_CONTENT_LENGTH = 5000;

    public static final Duration CACHE_DURATION = Duration.ofMinutes(5);

    public static final Duration CACHE_VIEWS_DURATION = CACHE_DURATION;
}
