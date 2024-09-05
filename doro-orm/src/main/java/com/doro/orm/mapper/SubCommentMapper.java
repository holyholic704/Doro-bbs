package com.doro.orm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doro.bean.CommentBean;
import com.doro.bean.SubCommentBean;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

/**
 * 子评论
 *
 * @author jiage
 */
public interface SubCommentMapper extends BaseMapper<SubCommentBean> {

    @Select("<script>" +
            "SELECT id, user_id, content, parent_id, replied_id, replied_user_id, create_time FROM ( SELECT id, user_id, content, parent_id, replied_id, replied_user_id, create_time, ROW_NUMBER() OVER ( PARTITION BY parent_id ORDER BY create_time DESC ) AS num FROM doro_bbs_sub_comment WHERE parent_id IN ( " +
            "<foreach collection='ids' item='id' separator=','>" +
            " #{id} " +
            "</foreach>" +
            " ) AND del = 0 ) AS temp WHERE num &lt;= #{subCommentCount}" +
            "</script>")
    List<SubCommentBean> getByParentIds(Collection<Long> ids, int subCommentCount);
}
