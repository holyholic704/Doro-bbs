package com.doro.orm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doro.orm.bean.CommentBean;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 评论
 *
 * @author jiage
 */
public interface CommentMapper extends BaseMapper<CommentBean> {

    @Select("SELECT id, user_id, reply_id, content, create_time, del FROM doro_bbs_comment WHERE post_id = #{postId} AND reply_id = 0 ORDER BY create_time ASC LIMIT #{size}")
    @Results({
            @Result(property = "id", column = "id", id = true),
            @Result(property = "children", column = "id", javaType = List.class, many = @Many(select = "com.doro.orm.mapper.CommentMapper.getChildren"))
    })
    List<CommentBean> getBySize(long postId, int size);

    @Select("SELECT id, user_id, reply_id, content, create_time, del FROM doro_bbs_comment WHERE reply_id = #{replyId} ORDER BY create_time ASC LIMIT 5")
    List<CommentBean> getChildren(long replyId);
}
