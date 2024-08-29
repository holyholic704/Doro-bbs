package com.doro.orm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doro.orm.bean.CommentBean;
import org.apache.ibatis.annotations.*;

import java.util.Collection;
import java.util.List;

/**
 * 评论
 *
 * @author jiage
 */
public interface CommentMapper extends BaseMapper<CommentBean> {

//    @Select("SELECT id, user_id, reply_id, content, create_time FROM doro_bbs_comment WHERE post_id = #{postId} AND reply_id = 0 AND del = 0 ORDER BY create_time ASC LIMIT #{current}, #{size}")
//    List<CommentBean> page(long postId, int current, int size);
//
//    @Select("SELECT id, user_id, reply_id, content, create_time FROM doro_bbs_comment WHERE reply_id = #{replyId} AND del = 0 ORDER BY create_time ASC LIMIT 5")
//    @Results({
//            @Result(property = "id", column = "id", id = true),
//    })
//    List<CommentBean> getChildren(long replyId);
//
//    @Select("SELECT COUNT(1) FROM doro_bbs_comment WHERE reply_id = #{replyId} AND del = 0")
//    Long getChildrenCount(long replyId);

//    @Select("<script>" +
//            "SELECT id, user_id, reply_id, content, create_time FROM doro_bbs_comment " +
//            "<where>" +
//            " AND id IN (" +
//            "<foreach collection='ids' item='id' separator=','>" +
//            " #{id} " +
//            "</foreach>" +
//            ")" +
//            " AND del = 0 ORDER BY create_time ASC" +
//            "</where>" +
//            "</script>")
//    @Results({
//            @Result(property = "id", column = "id", id = true),
//            @Result(property = "children", column = "id", javaType = List.class, many = @Many(select = "com.doro.orm.mapper.CommentMapper.getChildren")),
//            @Result(property = "count", column = "id", javaType = Long.class, one = @One(select = "com.doro.orm.mapper.CommentMapper.getChildrenCount"))
//    })
    List<CommentBean> pageByIds(List<Long> ids);

    @Select("<script>" +
            "SELECT * FROM ( SELECT *, ROW_NUMBER() OVER ( PARTITION BY parent_id ORDER BY create_time DESC ) AS num FROM doro_bbs_comment WHERE parent_id IN ( " +
            "<foreach collection='ids' item='id' separator=','>" +
            " #{id} " +
            "</foreach>" +
            " ) AND del = 0 ) AS temp WHERE num &lt;= 5" +
            "</script>")
    List<CommentBean> subCommentList(Collection<Long> ids);

}
