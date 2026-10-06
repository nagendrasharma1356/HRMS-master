package com.payroll.Controller;

import com.payroll.Dto.AssignPayrollRequest;
import com.payroll.Dto.AssignedPayrollDto;
import com.payroll.Dto.PayrollDto;
import com.payroll.Dto.PayrollSummaryDto;
import com.payroll.Service.AssignedPayrollService;
import com.payroll.common.ApiResponse;
import com.payroll.common.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/assignedPayrolls")
public class AssignedPayrollController
{
 private final AssignedPayrollService assignedPayrollService;

    public AssignedPayrollController(AssignedPayrollService assignedPayrollService) {
        this.assignedPayrollService = assignedPayrollService;
    }

    @Operation(summary = "Assign a payroll to a user")
    @PutMapping("/assign")
    public ResponseEntity<ApiResponse<PayrollDto>> assignPayrollToUser(
            @RequestBody AssignPayrollRequest request) {

        PayrollDto dto = assignedPayrollService.assignPayrollToUser(
                request.getPayrollId(),
                request.getUserId(),
                request.getRole()
        );

        ApiResponse<PayrollDto> response = new ApiResponse<>(
                true,
                "Payroll assigned successfully",
                dto
        );

        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Get all assigned payrolls with optional filters and pagination")
    @GetMapping("all")
    public ResponseEntity<ApiResponse<PagedResponse<AssignedPayrollDto>>> getAllAssignedPayrolls(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PagedResponse<AssignedPayrollDto> response = assignedPayrollService.getAllAssignedPayrolls(month, year, pageable);

        return ResponseEntity.ok(ApiResponse.<PagedResponse<AssignedPayrollDto>>builder()
                .success(true)
                .message("Assigned payrolls fetched successfully")
                .data(response)
                .build());
    }

    @Operation(summary = "Get assigned payroll by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssignedPayrollDto>> getAssignedPayrollById(@PathVariable Long id) {
        AssignedPayrollDto dto = assignedPayrollService.getAssignedPayrollById(id);
        ApiResponse<AssignedPayrollDto> response = new ApiResponse<>(true, "Payroll fetched successfully", dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete assigned payroll by ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAssignedPayroll(@PathVariable Long id) {
        assignedPayrollService.deleteAssignedPayroll(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Payroll deleted successfully", null);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Get monthly leave salary summary for a user")
    @GetMapping("/monthlySummary")
    public ApiResponse getMonthlyLeaveSalarySummary(
            @RequestParam("userId") Long userId,
            @RequestParam("role") String role) {
        return assignedPayrollService.getMonthlyLeaveSalarySummary(userId, role);
    }

    @Operation(summary = "Get payroll summary for a user")
    @GetMapping("/summary/{userId}")
    public ResponseEntity<PayrollSummaryDto> getPayrollSummary(
            @PathVariable Long userId,
            @RequestParam String role
    ) {
        PayrollSummaryDto summary = assignedPayrollService.getPayrollSummaryForUser(userId, role);
        return ResponseEntity.ok(summary);
    }

}
