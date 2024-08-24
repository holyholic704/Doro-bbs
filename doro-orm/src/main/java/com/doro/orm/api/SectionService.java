package com.doro.orm.api;

import com.doro.orm.bean.SectionBean;

/**
 * 版块
 *
 * @author jiage
 */
public interface SectionService {

    boolean saveSection(SectionBean sectionBean);

    boolean hasName(String name);

    boolean hasSection(Long id);
}
