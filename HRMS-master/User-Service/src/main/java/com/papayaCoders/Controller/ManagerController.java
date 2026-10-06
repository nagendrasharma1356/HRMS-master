package com.papayaCoders.Controller;

import com.papayaCoders.Dto.Login.LoginRequest;
import com.papayaCoders.Dto.ManagerRequestDto;
import com.papayaCoders.Dto.ManagerResponseDto;
import com.papayaCoders.Service.ManagerService;
import com.papayaCoders.common.ApiResponse;
import com.papayaCoders.common.PagedResponse;
import com.papayaCoders.model.Manager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager")
@Tag(name = "Manager Controller", description = "Endpoints for managing Manager data")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    // Create Manager
    @Operation(summary = "Create a new Manager")
    @PostMapping("create")
    public ResponseEntity<ApiResponse<ManagerResponseDto>> create(@RequestBody ManagerRequestDto requestDto) {
        ApiResponse<ManagerResponseDto> apiResponse = managerService.create(requestDto);
        if (apiResponse.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        } else {
            return ResponseEntity.badRequest().body(
                    apiResponse.withDefaultErrorMessage("Failed to create manager")
            );
        }
    }

    // Get all Managers
    @Operation(summary = "Get all Managers")
    @GetMapping("all")
    public ResponseEntity<PagedResponse<ManagerResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(managerService.getAll(page, size));
    }

    // Get Manager by ID
    @Operation(summary = "Get Manager by ID")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<ManagerResponseDto>> getById(@PathVariable Long id) {
        ApiResponse<ManagerResponseDto> apiResponse = managerService.getById(id);
        if (apiResponse.isSuccess()) {
            return ResponseEntity.ok(apiResponse);
        } else {
            return ResponseEntity.badRequest().body(
                    apiResponse.withDefaultErrorMessage("Manager not found with ID: " + id)
            );
        }
    }

    // Search by Name
    @Operation(summary = "Search Managers by name")
    @GetMapping("search/{name}")
    public ResponseEntity<PagedResponse<ManagerResponseDto>> search(
            @PathVariable String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(managerService.searchByName(name, page, size));
    }

    // Update Manager
    @Operation(summary = "Update Manager by ID")
    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<ManagerResponseDto>> update(
            @PathVariable Long id,
            @RequestBody ManagerRequestDto requestDto) {
        ApiResponse<ManagerResponseDto> apiResponse = managerService.update(id, requestDto);
        if (apiResponse.isSuccess()) {
            return ResponseEntity.ok(apiResponse);
        } else {
            return ResponseEntity.badRequest().body(
                    apiResponse.withDefaultErrorMessage("Failed to update manager with ID: " + id)
            );
        }
    }

    // Delete Manager
    @Operation(summary = "Delete Manager by ID")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        ApiResponse<Void> apiResponse = managerService.delete(id);
        if (apiResponse.isSuccess()) {
            return ResponseEntity.ok(apiResponse);
        } else {
            return ResponseEntity.badRequest().body(
                    apiResponse.withDefaultErrorMessage("Failed to delete manager with ID: " + id)
            );
        }
    }

    // validate manager

    @Operation(summary = "Validate Manager login credentials")
    @PostMapping("/validate")
    public ResponseEntity<Manager> validateManager(@RequestBody LoginRequest request){
        Manager user = managerService.validateManager(request.getEmail(), request.getPassword());
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
