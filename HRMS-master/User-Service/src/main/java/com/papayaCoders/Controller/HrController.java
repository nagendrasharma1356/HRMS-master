package com.papayaCoders.Controller;

import com.papayaCoders.Dto.HrRequestDto;
import com.papayaCoders.Dto.HrResponseDto;
import com.papayaCoders.Dto.Login.LoginRequest;
import com.papayaCoders.Service.HrService;
import com.papayaCoders.common.ApiResponse;
import com.papayaCoders.common.PagedResponse;
import com.papayaCoders.model.Hr;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;

@RestController
@RequestMapping("/api/hr")
@Tag(name = "HR Controller", description = "APIs for managing HR records")
public class HrController {

    @Autowired
    private HrService hrService;

    // Create HR
    @Operation(summary = "Create HR", description = "Creates a new HR record")
    @PostMapping("create")
    public ResponseEntity<ApiResponse<HrResponseDto>> create(@Valid @RequestBody HrRequestDto requestDto) {
        ApiResponse<HrResponseDto> apiResponse = hrService.createHr(requestDto);
        if (apiResponse.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        } else {
            return ResponseEntity.badRequest().body(
                    apiResponse.withDefaultErrorMessage("Failed to create HR record")
            );
        }
    }

    // Get all HR records
    @Operation(summary = "Get all HRs", description = "Retrieve a paginated list of all HR records")
    @GetMapping("all")
    public ResponseEntity<PagedResponse<HrResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(hrService.getAll(page, size));
    }

    // Get HR by ID
    @Operation(summary = "Get HR by ID", description = "Retrieve a specific HR by their ID")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<HrResponseDto>> getById(@PathVariable Long id) {
        ApiResponse<HrResponseDto> apiResponse = hrService.getById(id);
        if (apiResponse.isSuccess()) {
            return ResponseEntity.ok(apiResponse);
        } else {
            return ResponseEntity.badRequest().body(
                    apiResponse.withDefaultErrorMessage("HR record not found with ID: " + id)
            );
        }
    }

    // Update HR
    @Operation(summary = "Update HR", description = "Update details of an existing HR")
    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<HrResponseDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody HrRequestDto requestDto) {
        ApiResponse<HrResponseDto> apiResponse = hrService.updateHr(id, requestDto);
        if (apiResponse.isSuccess()) {
            return ResponseEntity.ok(apiResponse);
        } else {
            return ResponseEntity.badRequest().body(
                    apiResponse.withDefaultErrorMessage("Failed to update HR with ID: " + id)
            );
        }
    }

    // Search HR by name
    @Operation(summary = "Search HRs by name", description = "Search HR records by name with pagination")
    @GetMapping("search/{name}")
    public ResponseEntity<PagedResponse<HrResponseDto>> search(
            @PathVariable String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(hrService.searchByName(name, page, size));
    }

    // Delete HR

    @Operation(summary = "Delete HR", description = "Delete an HR by ID")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        ApiResponse<Void> apiResponse = hrService.delete(id);
        if (apiResponse.isSuccess()) {
            return ResponseEntity.ok(apiResponse);
        } else {
            return ResponseEntity.badRequest().body(
                    apiResponse.withDefaultErrorMessage("Failed to delete HR with ID: " + id)
            );
        }
    }

    // validate HR- login
    @Operation(summary = "Validate HR Login", description = "Authenticate HR using email and password")
    @PostMapping("/validate")
    public ResponseEntity<Hr> validateHr(@RequestBody LoginRequest request){
        Hr user  = hrService.validateUser(request.getEmail(), request.getPassword());
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
