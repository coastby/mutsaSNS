package com.example.likelionmutsasnsproject.dto.user;

import com.example.likelionmutsasnsproject.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {
    private Integer userId;
    private String userName;

    public static UserJoinResponse from(User user){
        return new UserJoinResponse(user.getId(), user.getUserName());
    }
}
