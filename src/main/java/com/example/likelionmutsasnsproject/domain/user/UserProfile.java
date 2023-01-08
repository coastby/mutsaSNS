package com.example.likelionmutsasnsproject.domain.user;

import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserProfile {      //Resource Server마다 제공하는 정보가 다르므로 통일시키기 위한 profile
    private final String oauthId;
    private final String userName;
    private final String email;
    private final String imgUrl;
    public User toUser(){
        return User.builder()
                .oauthId(this.oauthId)
                .userName(this.userName)
                .email(this.email)
                .role(UserRole.ROLE_USER)
                .build();
    }
}
