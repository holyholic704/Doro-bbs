package com.doro.orm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doro.api.model.request.RequestPost;
import com.doro.bean.PostBean;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 帖子
 *
 * @author jiage
 */
public interface PostMapper extends BaseMapper<PostBean> {

    @Select("SELECT A.id, A.title, A.content, A.section_id, A.author_id, A.views, A.comments FROM doro_bbs_post A JOIN ( SELECT id FROM doro_bbs_post WHERE del = 0 ORDER BY create_time DESC, id ASC LIMIT #{current}, #{size} ) B ON A.id = B.id")
    List<PostBean> page(RequestPost requestPost, int current, int size);
}
