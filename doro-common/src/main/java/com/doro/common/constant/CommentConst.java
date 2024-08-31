package com.doro.common.constant;

import java.time.Duration;

/**
 * 评论相关常量
 *
 * @author jiage
 */
public class CommentConst {

    public static final int MAX_CONTENT_LENGTH = 500;

    public static final int NORMAL_PAGE_SIZE = 20;

    public static final int ID_GAP = 100;

    public static final int USE_MIN_ID_PAGE = NORMAL_PAGE_SIZE * ID_GAP;

    public static final Duration MIN_ID_CACHE_DURATION = Duration.ofMinutes(5);

    public static final int NORMAL_SUB_COMMENT_COUNT = 5;

}
