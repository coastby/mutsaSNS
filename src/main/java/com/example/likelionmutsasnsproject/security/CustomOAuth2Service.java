package com.example.likelionmutsasnsproject.security;

import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.user.OAuthAttributes;
import com.example.likelionmutsasnsproject.dto.user.UserProfile;
import com.example.likelionmutsasnsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest); //OAuth 서비스(google..)에서 가져온 유저 정보
        log.info("oAuth2User : {}", oAuth2User.toString());
        Map<String, Object> attributes = oAuth2User.getAttributes();   //유저 정보 Map에 담음
        log.info("attribue : {}", attributes.toString());

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //사용한 OAuth 서비스 이름
        //OAuth 서비스에 따라 유저정보를 공통된 class인 UserProfile 객체로 만들어 준다.
        UserProfile userProfile = OAuthAttributes.extract(registrationId, oAuth2User);

        User user = saveOrUpdate(userProfile);      //DB에 저장
        log.info("userName : {}", oAuth2User.getName());
//        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
//                .getUserInfoEndpoint().getUserNameAttributeName();      //OAuth 로그인 시 키(pK)가 되는 값
        return UserProfile.builder()
                .userName(userProfile.getUserName())
                .authorities(List.of(new SimpleGrantedAuthority(user.getRole().name())))
                .attributes(oAuth2User.getAttribute("response"))
                .build();
    }
    private User saveOrUpdate(UserProfile userProfile){
        String userName = userProfile.getUserName();
        User user = userRepository.findByUserName(userName)
                .map(m -> m.update(userName, (String) userProfile.getAttributes().get("email"))) //OAuth 서비스 유저정보 변경이 있으면 업데이트
                .orElse(userProfile.toUser());          //user가 없으면 새로운 user 생성
        return userRepository.save(user);
    }
}
