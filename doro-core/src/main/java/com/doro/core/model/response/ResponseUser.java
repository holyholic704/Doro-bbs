package com.doro.core.model.response;

import com.doro.bean.User;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jiage
 */
@Setter
@Getter
public class ResponseUser {

    private Long id;

    private String username;

    private String token;

    public ResponseUser(User user, String token) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.token = token;
    }

    public ResponseUser(String token) {
        this.token = token;
    }
}
