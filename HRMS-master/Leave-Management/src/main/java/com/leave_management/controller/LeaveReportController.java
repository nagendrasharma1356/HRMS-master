package com.leave_management.controller;

import com.leave_management.Dto.LeaveReportDTO;
import com.leave_management.common.ApiResponse;
import com.leave_management.service.LeaveReportService;
import com.leave_management.service.LeaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports/leaves")
@Tag(name = "Leave Report Controller", description = "Endpoints for leave reporting and summaries")
public class LeaveReportController {

    @Autowired
    private LeaveReportService leaveReportService;



    @Operation(summary = "Get Monthly Leave Report", description = "Returns a list of leave reports for the current month")
    @GetMapping("/monthly")
    public List<LeaveReportDTO> getMonthlyLeaveReport() {
        return leaveReportService.generateMonthlyReport();
    }
    @Operation(summary = "Get Leave Summary", description = "Returns the paid and unpaid leave summary of a user based on role")
    @GetMapping("/leaveSummary")
    public ResponseEntity<ApiResponse> getLeaveSummary(
            @RequestParam Long userId,
            @RequestParam String role) {
        Map<String, Object> summary = leaveReportService.calculatePaidLeavesFromRange(userId, role);
        return ResponseEntity.ok(new ApiResponse(true,"Leave summary fetched successfully", summary));
    }

}

