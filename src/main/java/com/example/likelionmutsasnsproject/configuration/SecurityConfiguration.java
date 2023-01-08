package com.example.likelionmutsasnsproject.configuration;

import com.example.likelionmutsasnsproject.security.*;
import com.example.likelionmutsasnsproject.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtUtil jwtUtil;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomOAuth2Service customOAuth2Service;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final OAuth2AuthenticationSuccessHandler OAuth2AuthenticationSuccessHandler;

    private final String[] SWAGGER = {
                "/v3/api-docs",
                "/swagger-resources/**", "/configuration/security", "/webjars/**",
                "/swagger-ui.html", "/swagger/**", "/swagger-ui/**"};
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/hello/**").permitAll()
                .antMatchers(SWAGGER).permitAll()
                .antMatchers("/api/v1/users/join", "/api/v1/users/login","/api/v1/users/exception").permitAll()
                .antMatchers("/api/v1/posts/my").authenticated()
                .antMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                .antMatchers("/api/v1/users/*/role/change").access("hasRole('ADMIN')")
                .antMatchers("/api/**").authenticated()
                .anyRequest().hasRole("ADMIN")

                .and()
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .oauth2Login()
                .userInfoEndpoint().userService(customOAuth2Service)    //provicer로부터 획득한 유저정보를 다룰 service단을 지정한다.
                .and()
                .successHandler(OAuth2AuthenticationSuccessHandler)      //OAuth2 로그인 성공 시 호출한 handler
//                .failureHandler(authenticationFailureHandelr)
                .and()
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtFilter.class)
                .build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            private final long MAX_AGE_SECS = 3600;

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(MAX_AGE_SECS);
            }
        };
    }
}
