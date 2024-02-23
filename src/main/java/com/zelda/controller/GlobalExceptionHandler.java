package com.zelda.controller;

import com.zelda.exception.AppException;
import com.zelda.exception.InputInvalidException;
import com.zelda.exception.PostNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({AppException.class, InputInvalidException.class})  // Có thể bắt nhiều loại exception
    public ResponseEntity<String> handleCustomException(AppException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({PostNotFoundException.class})  // Có thể bắt nhiều loại exception
    public ResponseEntity<String> handlePostNotFoundException(AppException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnwantedException(Exception e) {
        // Log lỗi ra và ẩn đi message thực sự (xem phần 3.2)
        e.printStackTrace();  // Thực tế người ta dùng logger
        return ResponseEntity.status(500).body("Unknow error");
    }
}
