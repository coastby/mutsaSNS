package com.example.likelionmutsasnsproject.dto;

import com.example.likelionmutsasnsproject.domain.User;
import lombok.Getter;

@Getter
public class UserJoinRequest {
    private String userName;
    private String password;

    public User toEntity() {
        return User.builder()
                .userName(this.userName)
                .password(this.password)
                .role(UserRole.USER)
                .build();
    }
}
