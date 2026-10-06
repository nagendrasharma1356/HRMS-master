package com.payroll.exception;



import com.payroll.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiResponse response = new ApiResponse();
        response.setMessage(ex.getMessage());
        response.setSuccess(false);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequest(BadRequestException ex) {
        ApiResponse response = new ApiResponse();
        response.setMessage(ex.getMessage());
        response.setSuccess(false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(PayrollAlreadyAssignedException.class)
    public ResponseEntity<Map<String, Object>> handlePayrollAlreadyAssigned(
            PayrollAlreadyAssignedException ex,
            HttpServletRequest request) {

        Map<String, Object> errorBody = new HashMap<>();

        errorBody.put("status", false);
        errorBody.put("message", ex.getMessage());


        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }

}

