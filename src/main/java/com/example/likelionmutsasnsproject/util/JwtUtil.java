package com.example.likelionmutsasnsproject.util;

import com.example.likelionmutsasnsproject.dto.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
    @Value("${jwt.token.secret}")
    private String secretKey;
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
}
