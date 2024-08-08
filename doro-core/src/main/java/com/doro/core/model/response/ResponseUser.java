package com.doro.core.model.response;

import com.doro.bean.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
