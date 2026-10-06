package com.papayaCoders.Controller;

import com.papayaCoders.Dto.EmployeeRequestDto;
import com.papayaCoders.Dto.EmployeeResponseDto;
import com.papayaCoders.Dto.Login.LoginRequest;
import com.papayaCoders.Service.EmployeeService;
import com.papayaCoders.common.ApiResponse;
import com.papayaCoders.common.PagedResponse;
import com.papayaCoders.model.Employee;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(name = "Employee Controller", description = "APIs for managing employee operations")
@RestController
@RequestMapping("api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Create
    @Operation(summary = "Create Employee", description = "Creates a new employee")
    @PostMapping("create")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> create(@Valid @RequestBody EmployeeRequestDto requestDto) {
        ApiResponse<EmployeeResponseDto> apiResponse = employeeService.create(requestDto);
        if (apiResponse.isSuccess()) {
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(apiResponse.withDefaultErrorMessage("Failed to register employee"), HttpStatus.BAD_REQUEST);
        }
    }

    // Get All
    @Operation(summary = "Get All Employees", description = "Returns paginated list of employees")
    @GetMapping("all")
    public ResponseEntity<ApiResponse<PagedResponse<EmployeeResponseDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            PagedResponse<EmployeeResponseDto> response = employeeService.getAll(page, size);
            return ResponseEntity.ok(new ApiResponse<>(true, "Employees fetched successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching employee list"));
        }
    }

    // Get by ID
    @Operation(summary = "Get Employee by ID", description = "Returns employee based on ID")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> getById(@PathVariable Long id) {
        ApiResponse<EmployeeResponseDto> apiResponse = employeeService.getById(id);
        return apiResponse.isSuccess()
                ? new ResponseEntity<>(apiResponse, HttpStatus.OK)
                : new ResponseEntity<>(apiResponse.withDefaultErrorMessage("Employee not found"), HttpStatus.BAD_REQUEST);
    }

    // Search by Name
    @Operation(summary = "Search Employee by Name", description = "Returns employee list filtered by name")
    @GetMapping("searchName/{name}")
    public ResponseEntity<ApiResponse<PagedResponse<EmployeeResponseDto>>> searchByName(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable String name) {

        try {
            PagedResponse<EmployeeResponseDto> response = employeeService.searchByName(name, page, size);
            return ResponseEntity.ok(new ApiResponse<>(true, "Search results", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error during name search"));
        }
    }

    // Search by Aadhaar
    @Operation(summary = "Search Employee by Aadhaar", description = "Returns employee based on Aadhaar number")
    @GetMapping("searchAadhaar/{aadhaarNumber}")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> getByAadhaar(@PathVariable Long aadhaarNumber) {
        ApiResponse<EmployeeResponseDto> apiResponse = employeeService.searchByAadhaar(aadhaarNumber);
        return apiResponse.isSuccess()
                ? new ResponseEntity<>(apiResponse, HttpStatus.OK)
                : new ResponseEntity<>(apiResponse.withDefaultErrorMessage("Employee not found"), HttpStatus.BAD_REQUEST);
    }

    // Update
    @Operation(summary = "Update Employee", description = "Updates employee data for given ID")
    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> update(@PathVariable Long id,
                                                                   @RequestBody EmployeeRequestDto requestDto) {
        ApiResponse<EmployeeResponseDto> apiResponse = employeeService.update(id, requestDto);
        return apiResponse.isSuccess()
                ? new ResponseEntity<>(apiResponse, HttpStatus.OK)
                : new ResponseEntity<>(apiResponse.withDefaultErrorMessage("Update failed"), HttpStatus.BAD_REQUEST);
    }

    // Delete
    @Operation(summary = "Delete Employee", description = "Deletes employee by ID")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        ApiResponse<Void> apiResponse = employeeService.delete(id);
        return apiResponse.isSuccess()
                ? new ResponseEntity<>(apiResponse, HttpStatus.OK)
                : new ResponseEntity<>(apiResponse.withDefaultErrorMessage("Delete failed"), HttpStatus.BAD_REQUEST);
    }
    @Operation(summary = "Validate Employee Login", description = "Validates employee credentials")
    @PostMapping("/validate")
    public ResponseEntity<Employee> validateHr(@RequestBody LoginRequest request){
        Employee user  = employeeService.validateUser(request.getEmail(), request.getPassword());
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
