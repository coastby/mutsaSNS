package com.example.likelionmutsasnsproject.dto.user;

import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class UserProfile implements OAuth2User{      //Resource Server마다 제공하는 정보가 다르므로 통일시키기 위한 profile
    private String userName; //authentication의 name
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes; // oauthId, name, email
    public User toUser(){
        return User.builder()
                .oauthId((String) this.attributes.get("oauthId"))
                .userName(this.userName)
                .email((String) this.attributes.get("email"))
                .name((String) this.attributes.get("name"))
                .role(UserRole.ROLE_USER)
                .build();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return this.userName;
    }
}
