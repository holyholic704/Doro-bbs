package com.doro.orm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doro.api.model.request.RequestComment;
import com.doro.bean.CommentBean;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 评论
 *
 * @author jiage
 */
public interface CommentMapper extends BaseMapper<CommentBean> {

    @Select("SELECT A.id, A.user_id, A.post_id, A.content, A.comments, A.create_time FROM doro_bbs_comment A JOIN ( SELECT id FROM doro_bbs_comment WHERE post_id = #{requestComment.postId} AND del = 0 ORDER BY create_time ASC, id ASC LIMIT #{current}, #{size} ) B ON A.id = B.id")
    List<CommentBean> page(RequestComment requestComment, int current, int size);

    @Select("SELECT id, user_id, post_id, content, comments, create_time FROM doro_bbs_comment WHERE post_id = #{postId} AND del = 0 AND id >= #{minId} ORDER BY create_time ASC, id ASC LIMIT #{current}, #{size}")
    List<CommentBean> pageUseMinId(long postId, long minId, int current, int size);

    @Update("UPDATE doro_bbs_comment SET comments = ( " +
            "   SELECT count( 1 ) FROM doro_bbs_sub_comment WHERE parent_id = #{id} " +
            ") WHERE id = #{id} AND del = 0")
    Boolean updateComments(long id);

    @Select("SELECT id FROM ( SELECT ROW_NUMBER() over ( ORDER BY create_time ASC, id ASC ) rownum, id FROM doro_bbs_comment WHERE post_id = #{postId} AND del = 0 ) A WHERE rownum % #{idGap} = 1")
    List<Long> everyFewId(Long postId, int idGap);

    @Select("SELECT Z.* FROM ( " +
            "   SELECT " +
            "       X.id, " +
            "       X.user_id, " +
            "       X.post_id, " +
            "       X.content, " +
            "       X.comments, " +
            "       X.create_time, " +
            "       Y.id SUB_ID, " +
            "       Y.user_id SUB_USER_ID, " +
            "       Y.content SUB_CONTENT, " +
            "       Y.create_time SUB_TIME, " +
            "       ROW_NUMBER() over ( PARTITION BY X.id ORDER BY Y.create_time ASC ) rownum " +
            "   FROM ( " +
            "       SELECT A.id, A.user_id, A.post_id, A.content, A.comments, A.create_time " +
            "       FROM doro_bbs_comment A JOIN ( " +
            "           SELECT id FROM doro_bbs_comment WHERE post_id = #{postId} AND del = 0 ORDER BY create_time ASC, id ASC LIMIT #{current}, #{size} " +
            "       ) B ON A.id = B.id " +
            "   ) X LEFT JOIN doro_bbs_sub_comment Y ON X.id = Y.parent_id AND X.comments > 0 AND Y.del = 0 " +
            ") Z WHERE Z.rownum <= #{subCommentCount}")
    List<Map<String, Object>> getAllComment(long postId, int current, int size, int subCommentCount);
}
