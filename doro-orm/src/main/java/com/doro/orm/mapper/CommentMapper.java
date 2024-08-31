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

    @Select("SELECT A.id, A.user_id, A.post_id, A.content, A.comments, A.create_time FROM doro_bbs_comment A JOIN ( SELECT id FROM doro_bbs_comment WHERE post_id = #{postId} AND del = 0 ORDER BY create_time ASC, id ASC LIMIT #{current}, #{size} ) B ON A.id = B.id")
    List<CommentBean> page(Long postId, int current, int size);

    @Select("SELECT id, user_id, post_id, content, comments, create_time FROM doro_bbs_comment WHERE post_id = #{postId} AND del = 0 AND id >= #{minId} ORDER BY create_time ASC, id ASC LIMIT #{current}, #{size}")
    List<CommentBean> pageUseMinId(long postId, long minId, int current, int size);

    @Select("<script>" +
            "SELECT * FROM ( SELECT *, ROW_NUMBER() OVER ( PARTITION BY parent_id ORDER BY create_time DESC ) AS num FROM doro_bbs_comment WHERE parent_id IN ( " +
            "<foreach collection='ids' item='id' separator=','>" +
            " #{id} " +
            "</foreach>" +
            " ) AND del = 0 ) AS temp WHERE num &lt;= 5" +
            "</script>")
    List<CommentBean> subCommentList(Collection<Long> ids);

    @Select("SELECT id FROM ( SELECT ROW_NUMBER() over ( ORDER BY create_time ASC, id ASC ) rownum, id FROM doro_bbs_comment WHERE post_id = #{postId} AND del = 0 ) A WHERE rownum % #{idGap} = 1")
    List<Long> everyFewId(Long postId, int idGap);
}
