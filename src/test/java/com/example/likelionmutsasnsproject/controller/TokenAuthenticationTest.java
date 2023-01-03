package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.dto.user.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(value = {
        "jwt.token.secret=어떤툴을쓰더라도툴을잘쓰려면최소한단축키를잘알아야한다"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TokenAuthenticationTest {
    @Autowired
    private MockMvc mockMvc;
    private String secretKey="어떤툴을쓰더라도툴을잘쓰려면최소한단축키를잘알아야한다";
    private long expired_1hour = 1000 * 60 * 60;
    private long expired_1msec = 1;
    //token 생성하는 메서드
    private String generateToken_1hour(String userName, UserRole role){
        //claims 생성
        Claims claims = Jwts.claims();
        claims.put("userName", userName);
        claims.put("role", role.name());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expired_1hour))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
    private String generateToken_1msec(String userName, UserRole role){
        //claims 생성
        Claims claims = Jwts.claims();
        claims.put("userName", userName);
        claims.put("role", role.name());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expired_1msec))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
    private String token = generateToken_1hour("user", UserRole.ROLE_USER);

    @Test
    @Transactional
    @DisplayName("jwt 인증 성공")
    void authenticatedUser() throws Exception {
        mockMvc.perform(
                    get("/api/v1/auth-test-api")
                            .with(csrf())
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("good"))
                .andDo(print());
    }
    @Test
    @Transactional
    @DisplayName("jwt 인증 실패 - 헤더형식이상") // Bearer가 없을 때
    void authenticatedUser_헤더형식이상() throws Exception {
        mockMvc.perform(
                    get("/api/v1/auth-test-api")
                            .with(csrf())
                            .header(HttpHeaders.AUTHORIZATION, "" + token)) // Bearer가 없을 때
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result.errorCode").value("INVALID_PERMISSION"))
                .andExpect(jsonPath("$.result.message").exists())
                .andDo(print());
    }
    @Test
    @Transactional
    @DisplayName("jwt 인증 실패 - 유효하지않은jwt")
    void authenticatedUser_유효하지않은jwt() throws Exception {
        //1초만에 만료되는 토큰
        String token1msec = generateToken_1msec("user", UserRole.ROLE_USER);

        mockMvc.perform(
                        get("/api/v1/auth-test-api")
                                .with(csrf())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1msec))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result.errorCode").value("INVALID_TOKEN"))
                .andExpect(jsonPath("$.result.message").exists())
                .andDo(print());
    }
}
