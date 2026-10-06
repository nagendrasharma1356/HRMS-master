package com.papayaCoders.Controller;

import com.papayaCoders.Dto.AdminDto;
import com.papayaCoders.Dto.Login.LoginRequest;
import com.papayaCoders.Service.AdminService;
import com.papayaCoders.common.ApiResponse;
import com.papayaCoders.model.Admin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin Controller", description = "APIs for managing admin users")
@RestController
@RequestMapping("api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Operation(summary = "Create Admin", description = "Creates a new admin with the provided data.")

    @PostMapping("create")
    public ResponseEntity<ApiResponse<AdminDto>> create(@Valid @RequestBody AdminDto adminDto) {
        ApiResponse<AdminDto> apiResponse = adminService.create(adminDto);
        if (apiResponse.isSuccess()) {
            return ResponseEntity.status(201).body(apiResponse);
        } else {
            return ResponseEntity.badRequest().body(apiResponse.withDefaultErrorMessage("Failed to create admin"));
        }
    }

    @Operation(summary = "Delete Admin", description = "Deletes an admin by their ID.")

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        ApiResponse<Void> apiResponse = adminService.delete(id);
        if (apiResponse.isSuccess()) {
            return ResponseEntity.ok(apiResponse);
        } else {
            return ResponseEntity.badRequest().body(apiResponse.withDefaultErrorMessage("Failed to delete admin"));
        }
    }

    @Operation(summary = "Validate Admin Login", description = "Validates admin credentials and returns admin details if successful.")
    @PostMapping("/validate")
    public ResponseEntity<Admin> validateHr(@RequestBody LoginRequest request) {
        Admin user = adminService.validateUser(request.getEmail(), request.getPassword());
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
