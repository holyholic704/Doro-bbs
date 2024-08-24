package com.doro.core.service.post;

import com.doro.orm.service.PostInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiage
 */
@Service
public class CorePostInfoService {

    private final PostInfoService postInfoService;

    @Autowired
    public CorePostInfoService(PostInfoService postInfoService) {
        this.postInfoService = postInfoService;
    }
}
