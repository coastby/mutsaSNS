package com.example.likelionmutsasnsproject.security;

import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.exception.UserErrorCode;
import com.example.likelionmutsasnsproject.exception.UserException;
import com.example.likelionmutsasnsproject.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //헤더에서 토큰 꺼내기
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        //헤더 형식 확인
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            log.error("잘못된 헤더 형식 : {}", authorizationHeader);
            filterChain.doFilter(request, response);
            return;
        }

        //token 분리
        String token = null;
        token = authorizationHeader.trim().substring(7);
        //token을 authentication 만들기
        Authentication authentication = jwtUtil.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
