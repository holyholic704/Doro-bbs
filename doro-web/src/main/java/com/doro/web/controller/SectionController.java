package com.doro.web.controller;

import com.doro.common.enumeration.MessageEnum;
import com.doro.common.response.ResponseResult;
import com.doro.core.service.CoreSectionService;
import com.doro.api.model.request.RequestSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 版块管理
 *
 * @author jiage
 */
@RestController
@RequestMapping("section")
public class SectionController {

    private final CoreSectionService coreSectionService;

    @Autowired
    public SectionController(CoreSectionService coreSectionService) {
        this.coreSectionService = coreSectionService;
    }

    @PostMapping("save")
    public ResponseResult<?> saveSection(@RequestBody RequestSection requestSection) {
        return coreSectionService.saveSection(requestSection) ? ResponseResult.success(MessageEnum.SAVE_SUCCESS) : ResponseResult.error(MessageEnum.SAVE_ERROR);
    }
}
