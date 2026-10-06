package com.papayaCoders.controller;

import com.papayaCoders.common.ApiResponse;
import com.papayaCoders.common.PagedResponse;
import com.papayaCoders.dto.*;
import com.papayaCoders.exception.ResourceNotFoundException;
import com.papayaCoders.model.Role;
import com.papayaCoders.reposiory.RoleRepository;
import com.papayaCoders.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@Tag(name = "Role Controller", description = "Endpoints for managing role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper mapper;
    @Operation(summary = "Create a new role")
    @PostMapping("create")
    public ResponseEntity<ApiResponse<RoleResponseDto>> create(@RequestBody RoleDto roleDto) {
        ApiResponse<RoleResponseDto> response = roleService.create(roleDto);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(
                    response.withDefaultErrorMessage("Failed to create role")
            );
        }
    }

    // Update role
    @Operation(summary = "Update role by ID")
    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<RoleDto>> update(@PathVariable Long id, @RequestBody RoleDto roleDto) {
        ApiResponse<RoleDto> response = roleService.update(id, roleDto);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(
                    response.withDefaultErrorMessage("Failed to update role with ID: " + id)
            );
        }
    }

    // Get all roles
    @Operation(summary = "Get all roles (paginated)")
    @GetMapping("all")
    public ResponseEntity<PagedResponse<RoleAllResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(roleService.getAllRole(page, size));
    }

    // Get role by ID
    @Operation(summary = "Get role by ID")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<RoleDto>> getRoleById(@PathVariable Long id) {
        ApiResponse<RoleDto> response = roleService.getRoleById(id);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(
                    response.withDefaultErrorMessage("Role not found with ID: " + id)
            );
        }
    }

    // Delete role
    @Operation(summary = "Delete role by ID")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        ApiResponse<Void> response = roleService.delete(id);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(
                    response.withDefaultErrorMessage("Failed to delete role with ID: " + id)
            );
        }
    }

    // Remove permission from role
    @Operation(summary = "Remove permission from role")
    @PutMapping("/remove-permission")
    public ResponseEntity<ApiResponse<RoleResponseDto>> removePermissionFromRole(
            @RequestBody RemovePermissionRequest request
    ) throws BadRequestException {
        ApiResponse<RoleResponseDto> response = roleService.removePermissionFromRole(
                request.getRoleId(), request.getPermissionId()
        );
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Assign permissions to a role")
    @PutMapping("/permissions/{roleId}")
    public ApiResponse<RoleResponseDto> assignPermissionsToRole(
            @PathVariable Long roleId,
            @RequestBody PermissionAssignmentRequest request) {

        return roleService.assignPermissionsToRole(roleId, request.getPermissionIds());
    }



}
