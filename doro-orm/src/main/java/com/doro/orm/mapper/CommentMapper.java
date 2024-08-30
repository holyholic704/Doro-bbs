package com.doro.orm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doro.orm.bean.CommentBean;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

/**
 * 评论
 *
 * @author jiage
 */
public interface CommentMapper extends BaseMapper<CommentBean> {

    @Select("SELECT A.* FROM doro_bbs_comment A JOIN ( SELECT id FROM doro_bbs_comment WHERE post_id = #{postId} AND parent_id = 0 AND del = 0 ORDER BY create_time ASC LIMIT #{current}, #{size} ) B ON A.id = B.id")
    List<CommentBean> page(Long postId, int current, int size);

    @Select("SELECT id, user_id, post_id, content, parent_id, replied_id, replied_user_id, comments, create_time, update_time FROM doro_bbs_comment WHERE post_id = #{postId} AND parent_id = 0 AND del = 0 AND id >= #{minId} ORDER BY create_time ASC LIMIT #{current}, #{size}")
    List<CommentBean> pageUseMinId(long postId, long minId, int current, int size);
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
    @Select("SELECT\n" +
            "\t*,\n" +
            "\tROW_NUMBER() OVER ( PARTITION BY b.parent_id ORDER BY b.create_time DESC ) AS num \n" +
            "FROM\n" +
            "\t( SELECT * FROM doro_bbs_comment WHERE post_id = 582709450182661 AND parent_id = 0 LIMIT 10 ) a\n" +
            "\tLEFT JOIN doro_bbs_comment b ON a.id = b.parent_id")
    List<CommentBean> test();

    @Select("<script>" +
            "SELECT * FROM ( SELECT *, ROW_NUMBER() OVER ( PARTITION BY parent_id ORDER BY create_time DESC ) AS num FROM doro_bbs_comment WHERE parent_id IN ( " +
            "<foreach collection='ids' item='id' separator=','>" +
            " #{id} " +
            "</foreach>" +
            " ) AND del = 0 ) AS temp WHERE num &lt;= 5" +
            "</script>")
    List<CommentBean> subCommentList(Collection<Long> ids);

    @Select("SELECT id FROM ( SELECT @ROW := @ROW + 1 AS rownum, id FROM ( SELECT @ROW := 0 ) r, doro_bbs_comment WHERE post_id = #{postId} AND parent_id = 0 AND del = 0 ORDER BY create_time ASC ) ranked WHERE rownum % 100 = 1")
    List<Long> everyFewId(Long postId);
}
