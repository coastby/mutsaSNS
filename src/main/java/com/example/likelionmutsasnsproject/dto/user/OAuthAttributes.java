package com.example.likelionmutsasnsproject.dto.user;

import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;


@AllArgsConstructor
public enum OAuthAttributes {       //OAuth 서비스에 따라 얻어온 유저 정보의 key값이 다르기 때문에 각각 관리한다.
    GOOGLE("google", (oAuth2User) -> {
        return UserProfile.builder()
                .userName(oAuth2User.getName())
                .authorities(oAuth2User.getAuthorities())
                .attributes(
                        Map.of(
                                "oauthId", String.valueOf(oAuth2User.getAttributes().get("sub")),
                                "name", (String) oAuth2User.getAttributes().get("name"),
                                "email", (String) oAuth2User.getAttributes().get("email")
                        )
                )
                .build();

    }),
    NAVER("naver", (oAuth2User) -> {
        Map<String, String> attributes = oAuth2User.getAttribute("response");
        return UserProfile.builder()
                .userName(attributes.get("id"))
                .authorities(oAuth2User.getAuthorities())
                .attributes(
                        Map.of(
                                "oauthId", attributes.get("id"),
                                "name", attributes.get("name"),
                                "email", attributes.get("email")
                        )
                )
                .build();
    });
    private final String registrationId;
    private final Function<OAuth2User, UserProfile> setUserInfo;

    //OAuth 서비스의 정보를 통해 UserProfile을 얻는다.
    public static UserProfile extract(String registrationId, OAuth2User oAuth2User){
        return Arrays.stream(values())
                .filter(provider -> registrationId.equals(provider.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .setUserInfo.apply(oAuth2User);
    }
}
