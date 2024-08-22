package com.doro.core.model.response;

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

    public ResponseUser(Long id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
    }

    public ResponseUser(String token) {
        this.token = token;
    }
}
