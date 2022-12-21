package com.example.likelionmutsasnsproject.util;

import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.UserRole;
import com.example.likelionmutsasnsproject.exception.UserErrorCode;
import com.example.likelionmutsasnsproject.exception.UserException;
import com.example.likelionmutsasnsproject.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SignatureException;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${jwt.token.secret}")
    private String secretKey;
    private final UserService userService;
    private final long expiredTimeMs = 1000 * 60 * 60; // 60min

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
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(makeKey())
                .compact();
    }
    //token에서 claim 추출하는 메서드
    //token이 유효하지 않으면 custom exception throw
    public Claims extractClaims(String token){
        try{
            return Jwts.parserBuilder().setSigningKey(makeKey()).build().parseClaimsJws(token).getBody();
        } catch(ExpiredJwtException e){             //null값은 어디 걸리나?
            log.error("만료된 토큰입니다. : {}", token);
            throw new UserException(UserErrorCode.INVALID_TOKEN, "만료된 토큰입니다.");
        } catch(UnsupportedJwtException e){
            log.error("지원하지 않는 토큰입니다. : {}", token);
            throw new UserException(UserErrorCode.INVALID_TOKEN, "지원하지 않는 토큰입니다.");
        } catch(MalformedJwtException | IllegalArgumentException e){
            log.error("바르지 않은 형식의 토큰입니다. : {}", token);
            throw new UserException(UserErrorCode.INVALID_TOKEN, "바르지 않은 형식의 토큰입니다.");
        }
    }
    //token으로 authentication 꺼내는 메서드
    public Authentication getAuthentication(String token){
        String userName = extractClaims(token).get("userName", String.class);
        User user = userService.getUserByUserName(userName);        //exception 처리????
        return new UsernamePasswordAuthenticationToken(user.getUserName(), null,
                List.of(new SimpleGrantedAuthority(user.getRole().name())));
    }
}
