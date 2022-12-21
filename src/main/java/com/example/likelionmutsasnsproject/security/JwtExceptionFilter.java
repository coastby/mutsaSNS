package com.example.likelionmutsasnsproject.security;

import com.example.likelionmutsasnsproject.dto.Response;
import com.example.likelionmutsasnsproject.exception.ErrorResponse;
import com.example.likelionmutsasnsproject.exception.UserErrorCode;
import com.example.likelionmutsasnsproject.exception.UserException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

@Component
//토큰 관련 예외 처리
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            filterChain.doFilter(request, response);
        } catch (UserException e){
            response.setStatus(e.getUserErrorCode().getStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), Response.error(new ErrorResponse(e.getUserErrorCode().name(), e.toString())));
        }
    }
}
