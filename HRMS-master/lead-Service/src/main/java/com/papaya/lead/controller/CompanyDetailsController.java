package com.papaya.lead.controller;

import com.papaya.lead.config.ApiResponse;
import com.papaya.lead.config.PageResponse;
import com.papaya.lead.dto.CompanyDetailsDTO;
import com.papaya.lead.service.CompanyDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Swagger imports
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Tag(name = "Company Details", description = "Endpoints for managing company details")
public class CompanyDetailsController {

    private final CompanyDetailsService companyDetailsService;

    @Operation(
            summary = "Create a new company",
            description = "Creates a new company with the provided details",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Company created successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CompanyDetailsDTO>> createCompany(
            @Parameter(description = "Company details to create", required = true) @RequestBody CompanyDetailsDTO dto) {
        CompanyDetailsDTO created = companyDetailsService.createCompanyDetails(dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Company created successfully", created)
        );
    }

    @Operation(
            summary = "Get company by ID",
            description = "Fetch a company details by its ID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Company fetched successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Company not found")
            }
    )
    @GetMapping("/getById/{id}")
    public ResponseEntity<ApiResponse<CompanyDetailsDTO>> getCompanyById(
            @Parameter(description = "ID of the company", required = true) @PathVariable Long id) {
        CompanyDetailsDTO company = companyDetailsService.getCompanyDetailsById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Company fetched successfully", company)
        );
    }

    @Operation(
            summary = "Get all companies",
            description = "Fetch all companies without pagination",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "All companies fetched successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<CompanyDetailsDTO>>> getAllCompanies() {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "All companies fetched successfully", companyDetailsService.getAllCompanyDetails())
        );
    }

    @Operation(
            summary = "Update a company",
            description = "Update the company details identified by ID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Company updated successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Company not found")
            }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<CompanyDetailsDTO>> updateCompany(
            @Parameter(description = "ID of the company to update", required = true) @PathVariable Long id,
            @Parameter(description = "Updated company details") @RequestBody CompanyDetailsDTO dto) {
        CompanyDetailsDTO updated = companyDetailsService.updateCompanyDetails(id, dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Company updated successfully", updated)
        );
    }

    @Operation(
            summary = "Delete a company",
            description = "Delete the company identified by ID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Company deleted successfully"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Company not found")
            }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCompany(
            @Parameter(description = "ID of the company to delete", required = true) @PathVariable Long id) {
        companyDetailsService.deleteCompanyDetails(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Company deleted successfully", "Deleted")
        );
    }

    @Operation(
            summary = "Get paginated companies",
            description = "Fetch companies in a paginated way with page and size parameters",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Paginated companies fetched successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<PageResponse<CompanyDetailsDTO>>> getAllCompaniesPaginated(
            @Parameter(description = "Page number (default 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (default 5)") @RequestParam(defaultValue = "5") int size
    ) {
        PageResponse<CompanyDetailsDTO> response = companyDetailsService.getAllCompaniesPaginated(page, size);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Paginated companies fetched successfully", response)
        );
    }

    @Operation(
            summary = "Create company and assign to a lead",
            description = "Create a new company and link it to a lead by leadId",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Company created and linked to lead successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/create/lead/{leadId}")
    public ResponseEntity<ApiResponse<CompanyDetailsDTO>> createCompanyForLead(
            @Parameter(description = "Lead ID to link the company", required = true) @PathVariable Long leadId,
            @Parameter(description = "Company details to create") @RequestBody CompanyDetailsDTO dto
    ) {
        CompanyDetailsDTO created = companyDetailsService.createCompanyForLead(leadId, dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Company created and linked to lead successfully", created)
        );
    }

}