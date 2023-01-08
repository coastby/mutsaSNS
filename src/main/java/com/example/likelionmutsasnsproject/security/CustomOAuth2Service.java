package com.example.likelionmutsasnsproject.security;

import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.domain.user.OAuthAttributes;
import com.example.likelionmutsasnsproject.domain.user.UserProfile;
import com.example.likelionmutsasnsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class CustomOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest); //OAuth 서비스(google..)에서 가져온 유저 정보
        Map<String, Object>  attributes = oAuth2User.getAttributes();   //유저 정보 Map에 담음

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //사용한 OAuth 서비스 이름
        //OAuth 서비스에 따라 유저정보를 공통된 class인 UserProfile 객체로 만들어 준다.
        UserProfile userProfile = OAuthAttributes.extract(registrationId, attributes);

        User user = saveOrUpdate(userProfile);      //DB에 저장

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();      //OAuth 로그인 시 키(pK)가 되는 값

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority(user.getRole().name())),
                        attributes,
                        userNameAttributeName);
    }
    private User saveOrUpdate(UserProfile userProfile){
        User user = userRepository.findByOauthId(userProfile.getOauthId())
                .map(m -> m.update(userProfile.getUserName(), userProfile.getEmail())) //OAuth 서비스 유저정보 변경이 있으면 업데이트
                .orElse(userProfile.toUser());          //user가 없으면 새로운 user 생성
        return userRepository.save(user);
    }
}
