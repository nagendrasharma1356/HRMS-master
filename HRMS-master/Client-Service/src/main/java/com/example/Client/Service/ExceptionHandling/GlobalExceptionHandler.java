package com.example.Client.Service.ExceptionHandling;

import com.example.Client.Service.ExceptionHandling.ResourceNotFoundException;
import com.example.Client.Service.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .data(null)
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    // 2. Validation Exception (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .success(false)
                        .message("Validation failed: " + errorMessage)
                        .data(null)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    // 3. Parameter Type Mismatch (e.g. /id/abc instead of /id/1)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .success(false)
                        .message("Invalid value for parameter: " + ex.getName())
                        .data(null)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    // 4. Generic Exception Handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        ex.printStackTrace(); // Logging
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .success(false)
                        .message("Internal Server Error: " + ex.getMessage())
                        .data(null)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
