package com.doro.core.service;

import cn.hutool.core.util.StrUtil;
import com.doro.common.constant.SectionConst;
import com.doro.common.exception.ValidException;
import com.doro.core.utils.UserUtil;
import com.doro.api.orm.SectionService;
import com.doro.bean.SectionBean;
import com.doro.api.model.request.RequestSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 版块
 *
 * @author jiage
 */
@Service
public class CoreSectionService {

    private final SectionService sectionService;

    @Autowired
    public CoreSectionService(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    public boolean saveSection(RequestSection requestSection) {
        valid(requestSection);
        Long createUserId = UserUtil.getUserId();
        SectionBean sectionBean = new SectionBean()
                .setCreateUserId(createUserId)
                .setParentId(requestSection.getParentId())
                .setName(requestSection.getName());
        return sectionService.saveSection(sectionBean);
    }

    private void valid(RequestSection requestSection) {
        // TODO 版块校验规则
        if (StrUtil.isEmpty(requestSection.getName()) || requestSection.getName().length() > SectionConst.MAX_NAME_LENGTH) {
            throw new ValidException(String.format("请输入版块名称（1 ~ %s 个字）", SectionConst.MAX_NAME_LENGTH));
        }
        isNameExisted(requestSection.getName());
    }

    private void isNameExisted(String name) {
        if (sectionService.hasName(name)) {
            throw new ValidException("该版块已存在");
        }
    }
}
