package com.example.likelionmutsasnsproject.exception;

import com.example.likelionmutsasnsproject.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
@Slf4j
@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(PostException.class)
    public ResponseEntity<?> postExceptionHandler(PostException e){
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(new ErrorResponse(e.getErrorCode(), e.toString())));
    }
    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> userExceptionHandler(UserException e){
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(new ErrorResponse(e.getErrorCode(), e.toString())));
    }
    @ExceptionHandler(CommentException.class)
    public ResponseEntity<?> commentExceptionHandler(CommentException e){
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(new ErrorResponse(e.getErrorCode(), e.toString())));
    }
    @ExceptionHandler(LikeException.class)
    public ResponseEntity<?> likeExceptionHandler(LikeException e){
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(new ErrorResponse(e.getErrorCode(), e.toString())));
    }
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> sqlExceptionHandler(SQLException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(new ErrorResponse(ErrorCode.DATABASE_ERROR, e.getMessage())));
    }

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e){
//        log.error(e.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(Response.error(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage())));
//    }
}
