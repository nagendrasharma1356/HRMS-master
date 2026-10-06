package com.papayaCoder.Controller;

import com.papayaCoder.Dto.AuthResponse;
import com.papayaCoder.Dto.LoginRequest;
import com.papayaCoder.Service.AuthenticationService;
import com.papayaCoder.common.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Controller", description = "Handles user login and authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Operation(summary = "Login user and return JWT token")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = authenticationService.login(loginRequest);

            ApiResponse<AuthResponse> response = new ApiResponse<>(
                    true,
                    "Login successful",
                    authResponse
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            ApiResponse<AuthResponse> errorResponse = new ApiResponse<AuthResponse>()
                    .withDefaultErrorMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}
