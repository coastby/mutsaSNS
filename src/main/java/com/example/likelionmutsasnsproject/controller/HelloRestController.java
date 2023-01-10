package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.service.HelloService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@ApiIgnore
public class HelloRestController {
    private final HelloService helloService;
    @GetMapping(value = "/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok().body("조예지");
    }
    @Operation(summary = "security 테스트용", description = "USER 권한 이용자만 접근 가능 (포스트 작성과 동일)")
    @GetMapping(value = "/auth-test-api")
    public ResponseEntity<String> sayGood() {
        return ResponseEntity.ok().body("good");
    }
    @GetMapping(value = "/hello/{num}")
    public ResponseEntity<String> devide(@PathVariable int num) {
        return ResponseEntity.ok().body(helloService.sumOfDigit(num));
    }
}

