package com.doro.core.service.post;

import com.doro.orm.api.PostExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiage
 */
@Service
public class CorePostInfoService {

    private final PostExternalService postExternalService;

    @Autowired
    public CorePostInfoService(PostExternalService postExternalService) {
        this.postExternalService = postExternalService;
    }
}
