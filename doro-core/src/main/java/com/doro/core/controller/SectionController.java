package com.doro.core.controller;

import com.doro.common.response.ResponseResult;
import com.doro.core.service.CoreSectionService;
import com.doro.orm.model.request.RequestSection;
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
        return coreSectionService.saveSection(requestSection);
    }
}
