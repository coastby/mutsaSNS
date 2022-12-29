package com.example.likelionmutsasnsproject.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {
    public String sumOfDigit(int num){
        int sum = 0;
        while (num > 0){
            sum += num%10;
            num /= 10;
        }
        return sum+"";
    }
}
