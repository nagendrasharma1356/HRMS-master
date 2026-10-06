package com.leave_management.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.leave_management.Dto.LeaveMonthSummary;
import com.leave_management.common.PagedResponse;
import com.leave_management.Dto.LeaveDto;
import com.leave_management.Enum.LeaveAction;
import com.leave_management.common.ApiResponse;

import com.leave_management.service.LeaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
@Tag(name = "Leave Controller", description = "Endpoints for managing leave requests")
public class LeaveController {

    private final LeaveService leaveService;

    @Operation(summary = "Apply for leave")
    @PostMapping("apply")
    public ResponseEntity<ApiResponse<LeaveDto>> createLeave(
            @Valid @RequestPart("leave") String leaveJson,
            @RequestParam("file") MultipartFile file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            LeaveDto leaveDto = mapper.readValue(leaveJson, LeaveDto.class);

            LeaveDto savedLeave = leaveService.createLeave(leaveDto, file);

            // Return success true and message
            ApiResponse<LeaveDto> response = new ApiResponse<>(true,"Leave created successfully", savedLeave);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();


            ApiResponse<LeaveDto> response = new ApiResponse<>( false, e.getMessage(),null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }



    @Operation(summary = "Get all leaves with optional filters")
    @GetMapping("/all")
    public ResponseEntity<PagedResponse<LeaveDto>> getLeaves(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String session,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) Integer year
    ) {
        PagedResponse<LeaveDto> response = leaveService.getAllLeaves(page, size, session, month, year);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get leave by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LeaveDto>> getLeaveById(@PathVariable Long id) {
        LeaveDto leaveDto = leaveService.getLeaveById(id);
        ApiResponse<LeaveDto> response = new ApiResponse<>(true, "Leave found", leaveDto);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Update leave by ID")
    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<LeaveDto>> updateLeave(
            @PathVariable Long id,
            @RequestBody LeaveDto leaveDto
    ) {
        LeaveDto updatedLeave = leaveService.updateLeave(id, leaveDto);
        ApiResponse<LeaveDto> response = new ApiResponse<>(true, "Leave updated successfully", updatedLeave);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Delete leave by ID")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLeave(@PathVariable Long id) {
        leaveService.deleteLeaveById(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Leave deleted successfully", null);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Update leave action status (e.g., APPROVED, REJECTED)")

    @PatchMapping("/action/{id}")
    public ResponseEntity<ApiResponse<LeaveDto>> updateAction(
            @PathVariable Long id,
            @RequestParam LeaveAction action) {

        LeaveDto updatedLeave = leaveService.updateLeaveAction(id, action);

        ApiResponse<LeaveDto> response = new ApiResponse<>(
                true,
                "Leave action updated successfully",

                updatedLeave
        );

        return ResponseEntity.ok(response);
    }



}
