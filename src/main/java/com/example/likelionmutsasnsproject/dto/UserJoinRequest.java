package com.example.likelionmutsasnsproject.dto;

import com.example.likelionmutsasnsproject.domain.User;
import lombok.Getter;

@Getter
public class UserJoinRequest {
    private String userName;
    private String password;

    public User toEntity(String encoded) {
        return User.builder()
                .userName(this.userName)
                .password(encoded)
                .role(UserRole.USER)
                .build();
    }
}
