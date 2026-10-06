package com.papaya.lead.controller;

import com.papaya.lead.config.ApiResponse;
import com.papaya.lead.config.PageResponse;
import com.papaya.lead.dto.DealsDTO;
import com.papaya.lead.service.DealsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Swagger OpenAPI annotations imports
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/deals")
@RequiredArgsConstructor
@Tag(name = "Deals", description = "Endpoints for managing deals")
public class DealsController {

    private final DealsService dealsService;

    @Operation(
            summary = "Create a deal linked to a Lead Contact",
            description = "Creates a new deal for the given lead contact ID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Deal created successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Lead Contact not found")
            }
    )
    @PostMapping("create/leadId/{leadId}")
    public ResponseEntity<ApiResponse<DealsDTO>> createDeal(
            @Parameter(description = "ID of the lead contact", required = true) @PathVariable Long leadId,
            @Parameter(description = "Deal details") @RequestBody DealsDTO dto) {
        DealsDTO created = dealsService.createDeal(leadId, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Deal created successfully", created));
    }


    @Operation(
            summary = "Get deal by ID",
            description = "Fetches a deal by its unique ID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Deal fetched successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Deal not found")
            }
    )
    @GetMapping("getDealById/{id}")
    public ResponseEntity<ApiResponse<DealsDTO>> getDealById(
            @Parameter(description = "ID of the deal", required = true) @PathVariable Long id) {
        DealsDTO deal = dealsService.getDealById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Deal fetched successfully", deal));
    }

    @Operation(
            summary = "Get all deals",
            description = "Fetches all deals without pagination",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "All deals fetched successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<DealsDTO>>> getAllDeals() {
        return ResponseEntity.ok(new ApiResponse<>(true, "All deals fetched successfully", dealsService.getAllDeals()));
    }

    @Operation(
            summary = "Get deals by Lead Contact ID",
            description = "Fetches all deals associated with a specific lead contact ID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Deals for lead fetched successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/getDealsBy/leadId/{leadId}")
    public ResponseEntity<ApiResponse<List<DealsDTO>>> getDealsByLead(
            @Parameter(description = "ID of the lead contact", required = true)
            @PathVariable Long leadId) {

        List<DealsDTO> deals = dealsService.getDealsByLeadContactId(leadId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Deals for lead fetched successfully", deals));
    }


    @Operation(
            summary = "Update deal by ID",
            description = "Updates an existing deal by its ID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Deal updated successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Deal not found")
            }
    )
    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<DealsDTO>> updateDeal(
            @Parameter(description = "ID of the deal", required = true) @PathVariable Long id,
            @Parameter(description = "Updated deal details") @RequestBody DealsDTO dto) {
        DealsDTO updated = dealsService.updateDeal(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Deal updated successfully", updated));
    }

    @Operation(
            summary = "Delete deal by ID",
            description = "Deletes a deal by its ID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Deal deleted successfully"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Deal not found")
            }
    )
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteDeal(
            @Parameter(description = "ID of the deal", required = true) @PathVariable Long id) {
        dealsService.deleteDeal(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Deal deleted successfully", "Deleted"));
    }

    @Operation(
            summary = "Get deals by Company ID",
            description = "Fetches all deals associated with a specific company ID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Deals by company ID fetched successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                            )
                    )
            }
    )
    @GetMapping("getDealsBy/companyId/{companyId}")
    public ResponseEntity<ApiResponse<List<DealsDTO>>> getDealsByCompany(
            @Parameter(description = "ID of the company", required = true) @PathVariable Long companyId) {
        List<DealsDTO> deals = dealsService.getDealsByCompanyId(companyId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Deals by company ID fetched successfully", deals));
    }

    @Operation(
            summary = "Get paginated deals",
            description = "Fetches deals paginated with page and size parameters",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Paginated deals fetched successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<PageResponse<DealsDTO>>> getAllDealsPaginated(
            @Parameter(description = "Page number, default 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size, default 5") @RequestParam(defaultValue = "5") int size
    ) {
        PageResponse<DealsDTO> pageResponse = dealsService.getAllDealsPaginated(page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "Paginated deals fetched successfully", pageResponse));
    }

}