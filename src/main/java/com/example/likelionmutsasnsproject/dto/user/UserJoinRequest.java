package com.example.likelionmutsasnsproject.dto.user;

import com.example.likelionmutsasnsproject.domain.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class UserJoinRequest {
    private String userName;
    private String password;

    public User toEntity(String encoded) {
        return User.builder()
                .userName(this.userName)
                .password(encoded)
                .role(UserRole.ROLE_USER)
                .build();
    }
}
