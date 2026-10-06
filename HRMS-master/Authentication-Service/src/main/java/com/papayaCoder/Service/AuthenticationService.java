package com.papayaCoder.Service;

import com.papayaCoder.Dto.AuthResponse;
import com.papayaCoder.Dto.LoginRequest;
import com.papayaCoder.Dto.UserDto;
import com.papayaCoder.Utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Service
public class AuthenticationService {

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private JwtHelper jwtHelper;

    public AuthResponse login(LoginRequest loginRequest) {
        if (loginRequest == null) {
            throw new IllegalArgumentException("Login request cannot be null");
        }
        System.out.println("Login request: " + loginRequest);

        List<Supplier<ResponseEntity<UserDto>>> validators = Arrays.asList(
                () -> userServiceClient.validateAdmin(loginRequest),
                () -> userServiceClient.validateHr(loginRequest),
                () -> userServiceClient.validateManager(loginRequest),
                () -> userServiceClient.validateEmployee(loginRequest)
        );

        for (Supplier<ResponseEntity<UserDto>> validator : validators) {
            try {
                ResponseEntity<UserDto> response = validator.get();
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    UserDto user = response.getBody();
                    String token = jwtHelper.generateToken(user.getEmail(), user.getRole());
                    return new AuthResponse(token, user.getRole());
                }
            } catch (Exception ex) {
                System.out.println("Login request: " + ex);
            }
        }

        // If none of the services validated the user
        throw new RuntimeException("Invalid credentials or user not found in any role.");
    }


}