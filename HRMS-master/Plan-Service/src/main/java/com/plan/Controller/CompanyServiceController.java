package com.plan.Controller;

import com.plan.Common.ApiResponse;
import com.plan.Common.PageResponse;
import com.plan.Dto.CompanyServiceDto;
import com.plan.Service.CompanyServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companyServices")
@Tag(name = "Company Service API", description = "APIs for managing company-specific services or plans.")
public class CompanyServiceController {

    @Autowired
    private CompanyServices companyServices;


    @Operation(summary = "Create New Service", description = "Adds a new service or plan for the specified company.")
    @PostMapping("create")
    public ResponseEntity<ApiResponse<CompanyServiceDto>> createService(@RequestBody CompanyServiceDto dto) {
        CompanyServiceDto created = companyServices.createService(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Service created successfully", created));
    }

    @Operation(summary = "Get All Services", description = "Fetches a paginated list of all services/plans available.")
    @GetMapping("all")
    public ResponseEntity<ApiResponse<PageResponse<CompanyServiceDto>>> getAllServices(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {

        PageResponse<CompanyServiceDto> response = companyServices.getAllServices(page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "Services fetched successfully", response));
    }

    @Operation(summary = "Get Service by ID", description = "Fetches detailed information of a service/plan by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyServiceDto>> getServiceById(@PathVariable Long id) {
        CompanyServiceDto service = companyServices.getServiceById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Service fetched successfully", service));
    }

    @Operation(summary = "Update Service", description = "Updates existing service details based on provided ID.")
    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<CompanyServiceDto>> updateService(@PathVariable Long id, @RequestBody CompanyServiceDto dto) {
        CompanyServiceDto updated = companyServices.updateService(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Service updated successfully", updated));
    }

    @Operation(summary = "Delete Service", description = "Deletes a service/plan by its ID.")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteService(@PathVariable Long id) {
        companyServices.deleteService(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Service deleted successfully", "Deleted"));
    }
}
