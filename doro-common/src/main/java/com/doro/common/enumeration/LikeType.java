package com.doro.common.enumeration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 赞或踩的对象类型
 *
 * @author jiage
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum LikeType {

    /**
     * 帖子
     */
    POST((short) 0),

    /**
     * 评论
     */
    COMMENT((short) 1),

    /**
     * 二级评论
     */
    SUB_COMMENT((short) 2),
    ;

    /**
     * 类型编号
     */
    private final short type;

    /**
     * 根据类型编号获取具体的类型
     *
     * @param type 类型编号
     * @return 赞或踩的对象类型
     */
    public static LikeType getLikeType(Number type) {
        if (type != null) {
            LikeType[] types = values();
            for (LikeType likeType : types) {
                if (likeType.getType() == type.shortValue()) {
                    return likeType;
                }
            }
        }
        return null;
    }
}
