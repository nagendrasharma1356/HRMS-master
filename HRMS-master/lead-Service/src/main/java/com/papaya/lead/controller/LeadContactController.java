package com.papaya.lead.controller;

import com.papaya.lead.config.ApiResponse;
import com.papaya.lead.config.PageResponse;
import com.papaya.lead.dto.LeadContactDTO;
import com.papaya.lead.service.LeadContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Swagger imports
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
@Tag(name = "Lead Contacts", description = "Endpoints for managing lead contacts")
public class LeadContactController {

    private final LeadContactService leadContactService;

    @Operation(
            summary = "Create a new lead contact",
            description = "Creates a new lead contact with provided details",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Lead created successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<LeadContactDTO>> createLead(
            @Parameter(description = "Lead contact data to create", required = true) @RequestBody LeadContactDTO dto) {
        LeadContactDTO created = leadContactService.createLeadContact(dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Lead created successfully", created)
        );
    }

    @Operation(
            summary = "Get lead contact by ID",
            description = "Fetch a lead contact by its ID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Lead fetched successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Lead not found")
            }
    )
    @GetMapping("/getBy/{id}")
    public ResponseEntity<ApiResponse<LeadContactDTO>> getLeadById(
            @Parameter(description = "ID of the lead contact", required = true) @PathVariable Long id) {
        LeadContactDTO lead = leadContactService.getLeadContactById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Lead fetched successfully", lead)
        );
    }

    @Operation(
            summary = "Get all lead contacts",
            description = "Fetch all lead contacts without pagination",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "All leads fetched successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<LeadContactDTO>>> getAllLeads() {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "All leads fetched successfully", leadContactService.getAllLeadContacts())
        );
    }

    @Operation(
            summary = "Update a lead contact",
            description = "Update the lead contact identified by ID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Lead updated successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Lead not found")
            }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<LeadContactDTO>> updateLead(
            @Parameter(description = "ID of the lead contact to update", required = true) @PathVariable Long id,
            @Parameter(description = "Updated lead contact data") @RequestBody LeadContactDTO dto) {
        LeadContactDTO updated = leadContactService.updateLeadContact(id, dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Lead updated successfully", updated)
        );
    }

    @Operation(
            summary = "Delete a lead contact",
            description = "Delete the lead contact identified by ID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Lead deleted successfully"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Lead not found")
            }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteLead(
            @Parameter(description = "ID of the lead contact to delete", required = true) @PathVariable Long id) {
        leadContactService.deleteLeadContact(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Lead deleted successfully", "Deleted")
        );
    }

    @Operation(
            summary = "Get paginated lead contacts",
            description = "Fetch lead contacts in a paginated way with page and size params",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Paginated leads fetched successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<PageResponse<LeadContactDTO>>> getAllLeadsPaginated(
            @Parameter(description = "Page number (default 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (default 5)") @RequestParam(defaultValue = "5") int size
    ) {
        PageResponse<LeadContactDTO> pagedLeads = leadContactService.getAllLeadsPaginated(page, size);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Paginated leads fetched successfully", pagedLeads)
        );
    }


}