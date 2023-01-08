package com.example.likelionmutsasnsproject.domain.user;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
public enum OAuthAttributes {       //OAuth 서비스에 따라 얻어온 유저 정보의 key값이 다르기 때문에 각각 관리한다.
    GOOGLE("google", (attributes) -> {
        return UserProfile.builder()
                .oauthId(String.valueOf(attributes.get("sub")))
                .userName((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .build();
    }),
    NAVER("naver", (attributes) -> {
        return UserProfile.builder()
                .oauthId(String.valueOf(attributes.get("id")))
                .userName((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .build();
    }),
    KAKAO("kakao", (attributes) -> {
        return UserProfile.builder()
                .oauthId(String.valueOf(attributes.get("id")))
                .userName((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .build();
    });

    private final String registrationId;
    private final Function<Map<String, Object>, UserProfile> of;

    //OAuth 서비스의 정보를 통해 UserProfile을 얻는다.
    public static UserProfile extract(String registrationId, Map<String, Object> attributes){
        return Arrays.stream(values())
                .filter(provider -> registrationId.equals(provider.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}
