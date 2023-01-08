package com.example.likelionmutsasnsproject.util;

import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.user.UserRole;
import com.example.likelionmutsasnsproject.exception.ErrorCode;
import com.example.likelionmutsasnsproject.exception.UserException;
import com.example.likelionmutsasnsproject.repository.UserRepository;
import com.example.likelionmutsasnsproject.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${jwt.token.secret}")
    private String secretKey;
    @Value("${jwt.token.refresh}")
    private String refreshKey;
    private final UserRepository userRepository;
    private final long accessExpiredTimeMs = 1000 * 60 * 60; // 60min
    private final long refreshExpirredTimeMs = 1000 * 60 * 60 * 24 * 7; // 일주일

    //key를 만드는 메서드
    private Key makeKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    //token 생성하는 메서드
    public String generateToken(String userName, UserRole role){
        //claims 생성
        Claims claims = Jwts.claims();
        claims.put("userName", userName);
        claims.put("role", role.name());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiredTimeMs))
                .signWith(makeKey())
                .compact();
    }
    public void generateRefreshToken(Authentication authentication, HttpServletResponse response){
        String refreshToken = Jwts.builder()
                .signWith(makeKey())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirredTimeMs))
                .compact();
        saveRefreshToken(authentication, refreshToken);     //refreshToken DB에 저장
        log.debug("refreshToken : {}", refreshToken);
        ResponseCookie cookie = ResponseCookie.from(refreshKey, refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .maxAge(refreshExpirredTimeMs/1000)
                .path("/")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
    private void saveRefreshToken(Authentication authentication, String refreshToken) {
        String userName = authentication.getPrincipal().toString();
        userRepository.updateRefreshToken(userName, refreshToken);
    }

    //token에서 claim 추출하는 메서드
    //token이 유효하지 않으면 custom exception throw
    public Claims extractClaims(String token){
        try{
            return Jwts.parserBuilder().setSigningKey(makeKey()).build().parseClaimsJws(token).getBody();
        } catch (SignatureException e) {
            log.error("서명이 잘못된 토큰입니다. : {}", token);
            throw new UserException(ErrorCode.INVALID_TOKEN, "서명이 잘못된 토큰입니다.");
        } catch(ExpiredJwtException e){
            log.error("만료된 토큰입니다. : {}", token);
            throw new UserException(ErrorCode.INVALID_TOKEN, "만료된 토큰입니다.");
        } catch(UnsupportedJwtException e){
            log.error("지원하지 않는 토큰입니다. : {}", token);
            throw new UserException(ErrorCode.INVALID_TOKEN, "지원하지 않는 토큰입니다.");
        } catch(MalformedJwtException | IllegalArgumentException e){
            log.error("바르지 않은 형식의 토큰입니다. : {}", token);
            throw new UserException(ErrorCode.INVALID_TOKEN, "바르지 않은 형식의 토큰입니다.");
        }
    }
    //token으로 authentication 꺼내는 메서드
    public UsernamePasswordAuthenticationToken getAuthentication(String token){
        String userName = extractClaims(token).get("userName", String.class);
        User user;
        try{
            user = userRepository.findByUserName(userName)
                    .orElseThrow(() -> new UserException(ErrorCode.USERNAME_NOT_FOUND));
        } catch (UserException e){
            throw new UserException(e.getErrorCode(), "유효하지 않은 아이디입니다.");
        }
        return new UsernamePasswordAuthenticationToken(user.getUserName(), null,
                List.of(new SimpleGrantedAuthority(user.getRole().name())));
    }
}
