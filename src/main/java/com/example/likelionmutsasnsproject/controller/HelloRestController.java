package com.example.likelionmutsasnsproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HelloRestController {
    @GetMapping(value = "/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok().body("popin");
    }
    @GetMapping(value = "/good")
    public ResponseEntity<String> sayGood() {
        return ResponseEntity.ok().body("good");
    }
    @GetMapping(value = "/nice")
    public ResponseEntity<String> sayNice() {
        return ResponseEntity.ok().body("nice");
    }
}
