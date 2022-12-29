package com.example.likelionmutsasnsproject.service;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloServiceTest {
    private HelloService helloService;

    @BeforeEach
    void setUp(){
        helloService = new HelloService();
    }

    @Test
    @DisplayName("자리수의 합")
    void sum_of_digit(){
        int num = 12345;

        String str = helloService.sumOfDigit(num);

        assertEquals("15", str);
    }
}