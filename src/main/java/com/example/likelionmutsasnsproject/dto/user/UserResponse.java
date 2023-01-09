package com.example.likelionmutsasnsproject.dto.user;

import com.example.likelionmutsasnsproject.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Integer id;
    private String userName;
    public static UserResponse from(User user){
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .build();
    }
}
