package com.example.Client.Service.controller;

import com.example.Client.Service.common.ApiResponse;
import com.example.Client.Service.common.PagedResponse;
import com.example.Client.Service.dto.CompanyDto;
import com.example.Client.Service.service.CompanyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    // 1. Create Company
    @PostMapping("create")
    public ResponseEntity<ApiResponse<CompanyDto>> createCompany(@RequestBody CompanyDto companyDto) {
        CompanyDto createdCompany = companyService.createCompany(companyDto);
        ApiResponse<CompanyDto> apiResponse = new ApiResponse<>(true, "Company created successfully", createdCompany);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("assignedClient/{companyId}")
    public ResponseEntity<ApiResponse<CompanyDto>> assignClients(@PathVariable Long companyId, @RequestBody List<Long> clientIds) {
        CompanyDto createdCompany = companyService.assignClientsToCompany(companyId,clientIds);
        ApiResponse<CompanyDto> apiResponse = new ApiResponse<>(true, "Client Assigned successfully ", createdCompany);
        return ResponseEntity.ok(apiResponse);

    }
    // 2. Get All Companies with Pagination
    @GetMapping("getAll")
    public ResponseEntity<ApiResponse<PagedResponse<CompanyDto>>> getAllCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<CompanyDto> pagedCompanies = companyService.getAllCompanies(page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "Companies fetched successfully", pagedCompanies));
    }

    @GetMapping("getById/{id}")
    public CompanyDto getCompanyById(@PathVariable Long id) {
        return companyService.getCompanyById(id);
    }

    // 4. Update Company
    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<CompanyDto>> updateCompany(
            @PathVariable Long id,
            @RequestBody CompanyDto companyDto) {
        CompanyDto updatedCompany = companyService.updateCompany(id, companyDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Company updated successfully", updatedCompany));
    }

    // 5. Delete Company
    @DeleteMapping("/Company/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Company deleted successfully", null));
    }
}

