package com.papayaCoders.controller;

import com.papayaCoders.common.ApiResponse;
import com.papayaCoders.common.PagedResponse;
import com.papayaCoders.dto.PermissionDto;
import com.papayaCoders.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permission")
@Tag(name = "Permission Controller", description = "Endpoints for managing Permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    // Create Permission
    @Operation(summary = "Create a new Permission")
    @PostMapping("create")
    public ResponseEntity<ApiResponse<PermissionDto>> createPermission(@RequestBody PermissionDto permissionDto) {
        ApiResponse<PermissionDto> apiResponse = permissionService.create(permissionDto);
        if (apiResponse.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        } else {
            return ResponseEntity.badRequest().body(
                    apiResponse.withDefaultErrorMessage("Failed to create permission")
            );
        }
    }

    // Update Permission
    @Operation(summary = "Update Permission by ID")
    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<PermissionDto>> update(
            @PathVariable Long id,
            @RequestBody PermissionDto permissionDto) {
        ApiResponse<PermissionDto> apiResponse = permissionService.update(id, permissionDto);
        if (apiResponse.isSuccess()) {
            return ResponseEntity.ok(apiResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    apiResponse.withDefaultErrorMessage("Failed to update permission with ID: " + id)
            );
        }
    }

    // Get All Permissions
    @Operation(summary = "Get all Permissions with pagination")
    @GetMapping("all")
    public ResponseEntity<PagedResponse<PermissionDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(permissionService.getAll(page, size));
    }

    // Get Permission by ID
    @Operation(summary = "Get Permission by ID")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<PermissionDto>> getById(@PathVariable Long id) {
        ApiResponse<PermissionDto> apiResponse = permissionService.getById(id);
        if (apiResponse.isSuccess()) {
            return ResponseEntity.ok(apiResponse);
        } else {
            return ResponseEntity.badRequest().body(
                    apiResponse.withDefaultErrorMessage("Permission not found with ID: " + id)
            );
        }
    }

    // Delete Permission
    @Operation(summary = "Delete Permission by ID")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        ApiResponse<Void> apiResponse = permissionService.delete(id);
        if (apiResponse.isSuccess()) {
            return ResponseEntity.ok(apiResponse);
        } else {
            return ResponseEntity.badRequest().body(
                    apiResponse.withDefaultErrorMessage("Failed to delete permission with ID: " + id)
            );
        }
    }

    // Search Permission by Name

    @Operation(summary = "Search Permission by Name")
    @PostMapping("search")
    public ResponseEntity<ApiResponse<PermissionDto>> searchByName(@RequestBody String name) {
        ApiResponse<PermissionDto> apiResponse = permissionService.searchByName(name);
        if (apiResponse.isSuccess()) {
            return ResponseEntity.ok(apiResponse);
        } else {
            return ResponseEntity.badRequest().body(
                    apiResponse.withDefaultErrorMessage("Permission not found with name: " + name)
            );
        }
    }


}
