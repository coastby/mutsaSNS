package com.example.likelionmutsasnsproject.controller;

import org.springframework.security.test.context.support.WithSecurityContext;

@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    String userName() default "user";
    String role() default "USER";

}
