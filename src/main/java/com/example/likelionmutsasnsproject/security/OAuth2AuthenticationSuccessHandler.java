package com.example.likelionmutsasnsproject.security;

import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.domain.user.UserProfile;
import com.example.likelionmutsasnsproject.dto.Response;
import com.example.likelionmutsasnsproject.dto.user.UserJoinResponse;
import com.example.likelionmutsasnsproject.dto.user.UserLoginResponse;
import com.example.likelionmutsasnsproject.dto.user.UserRole;
import com.example.likelionmutsasnsproject.exception.ErrorCode;
import com.example.likelionmutsasnsproject.exception.ErrorResponse;
import com.example.likelionmutsasnsproject.exception.UserException;
import com.example.likelionmutsasnsproject.util.CookieUtil;
import com.example.likelionmutsasnsproject.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.likelionmutsasnsproject.security.CookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${app.oauth2.authorizedRedirectUri}")
    private String redirectUri;
    private final JwtUtil jwtUtil;
    private final CookieAuthorizationRequestRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        String targetUrl = determineTargetUrl(request, response, authentication);
//        if(response.isCommitted()){
//            log.debug("Response has already been commited");
//            return;
//        }
        clearAuthenticationAttributes(request, response);
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);
        writeTokenResponse(response, authentication);
    }

    private void writeTokenResponse(HttpServletResponse response, Authentication authentication) throws IOException {
        //JWT 생성
//        String userName = User.getUserNameFromAuthentication(authentication); //-> 안 됨
        UserProfile user = (UserProfile) authentication.getPrincipal();
        String userName = user.getName();

        UserRole role = UserRole.ROLE_USER; //일단 다 user로 설정
        String accessToken = jwtUtil.generateToken(userName, role);
        jwtUtil.generateRefreshToken(authentication, response);

        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(),
                new UserLoginResponse(accessToken));
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
//    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
//        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
//                .map(Cookie::getValue);
//        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())){
//            throw new UserException(ErrorCode.INVALID_REQUEST, "redirect URIs are not matched");
//        }
//        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
//        log.debug(targetUrl);
//
//        //JWT 생성
//        UserProfile user = (UserProfile) authentication.getPrincipal();
//        String userName = user.getUserName();
//
//        UserRole role = UserRole.ROLE_USER; //일단 다 user로 설정
//        String accessToken = jwtUtil.generateToken(userName, role);
//        jwtUtil.generateRefreshToken(authentication, response);
//
//        return UriComponentsBuilder.fromUriString(targetUrl)
//                .queryParam("accessToken", accessToken)
//                .build().toUriString();
//    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        URI authorizedUri = URI.create(redirectUri);

        return (authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                        && authorizedUri.getPort() == clientRedirectUri.getPort());
    }
}
