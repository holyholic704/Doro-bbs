package com.doro.orm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doro.api.model.request.RequestPost;
import com.doro.bean.PostBean;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 帖子
 *
 * @author jiage
 */
public interface PostMapper extends BaseMapper<PostBean> {

    @Select("SELECT A.id, A.title, A.content, A.section_id, A.author_id, A.views, A.comments FROM doro_bbs_post A JOIN ( SELECT id FROM doro_bbs_post WHERE del = 0 ORDER BY create_time DESC, id ASC LIMIT #{current}, #{size} ) B ON A.id = B.id")
    List<PostBean> page(RequestPost requestPost, int current, int size);

    @Update("UPDATE doro_bbs_post SET comments = ( " +
            "   SELECT SUM( C.comments ) FROM (" +
            "       SELECT count( 1 ) comments FROM doro_bbs_comment WHERE post_id = #{id} AND del = 0 " +
            "           UNION ALL" +
            "       SELECT count( 1 ) comments FROM doro_bbs_comment A JOIN doro_bbs_sub_comment B ON A.post_id = #{id} AND A.del = 0 AND A.id = B.parent_id AND B.del = 0 " +
            "   ) C " +
            ") WHERE id = #{id} AND del = 0")
    boolean updateComments(Long id);
}
