package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.dto.UserRole;
import com.example.likelionmutsasnsproject.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
    private JwtUtil jwtUtil;
    private String secretKey="어떤툴을쓰더라도툴을잘쓰려면최소한단축키를잘알아야한다";
    private long expired_1hour = 1000 * 60 * 60;
    private long expired_1sec = 1000;
    //token 생성하는 메서드
    private String generateToken(String userName, UserRole role){
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


    private String token = generateToken("string", UserRole.USER);

    @Test
    @Transactional
    void authenticatedUser() throws Exception {
        mockMvc.perform(
                    get("/api/v1/good")
                            .with(csrf())
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("good"))
                .andDo(print());
    }
    @Test
    @Transactional
    void authenticatedUser2() throws Exception {
        mockMvc.perform(
                        get("/api/v1/good")
                                .with(csrf())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());
    }


}
