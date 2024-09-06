package com.doro.api.orm;

import com.doro.bean.SectionBean;

/**
 * 版块
 *
 * @author jiage
 */
public interface SectionService {

    boolean saveSection(SectionBean sectionBean);

    boolean delSection(Long id);

    boolean hasName(String name);

    boolean hasSection(Long id);
}
