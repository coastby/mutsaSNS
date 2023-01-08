package com.example.likelionmutsasnsproject.annotation;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    String value() default "user";
    String username() default "";
    String[] roles() default { "ROLE_USER" };
    String[] authorities() default {};
    String password() default "password";
//
//
//    String userName() default "user";
//    String role() default "ROLE_USER";

}
