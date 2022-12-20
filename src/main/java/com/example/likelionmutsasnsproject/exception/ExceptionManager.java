package com.example.likelionmutsasnsproject.exception;

import com.example.likelionmutsasnsproject.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(PostException.class)
    public ResponseEntity<?> postExceptionHandler(UserException e){
        return ResponseEntity.status(e.getUserErrorCode().getStatus())
                .body(Response.error(e.getUserErrorCode().name(), e.toString()));
    }
    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> userExceptionHandler(UserException e){
        return ResponseEntity.status(e.getUserErrorCode().getStatus())
                .body(Response.error(e.getUserErrorCode().name(), e.toString()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage()));
    }
}
