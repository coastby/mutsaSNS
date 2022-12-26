package com.example.likelionmutsasnsproject.configuration;

import com.example.likelionmutsasnsproject.security.CustomAuthenticationEntryPoint;
import com.example.likelionmutsasnsproject.security.JwtExceptionFilter;
import com.example.likelionmutsasnsproject.security.JwtFilter;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtUtil jwtUtil;
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
                .antMatchers("/api/v1/hello").permitAll()
                .antMatchers("/api/v1/users/join", "/api/v1/users/login","/api/v1/users/exception").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                .antMatchers("/api/v1/posts/**").authenticated()
                .antMatchers("/api/v1/good").authenticated()
                .anyRequest().hasRole("ADMIN")

//                .and()
//                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtFilter.class)
                .build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(SWAGGER);
    }
}
