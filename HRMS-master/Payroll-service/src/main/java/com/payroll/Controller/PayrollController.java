package com.payroll.Controller;

import com.payroll.Dto.AssignPayrollRequest;
import com.payroll.Dto.PayrollDto;
import com.payroll.enumClass.Status;
import com.payroll.Service.PayrollService;
import com.payroll.common.ApiResponse;
import com.payroll.common.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payrolls")
public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    // Create Payroll
    @Operation(summary = "Create a new payroll entry")
    @PostMapping("create")
    public ResponseEntity<ApiResponse<PayrollDto>> createPayroll(@Valid  @RequestBody PayrollDto payrollDto) {
        PayrollDto created = payrollService.create(payrollDto);
        return ResponseEntity.ok(ApiResponse.success("Payroll created successfully", created));
    }

    // Get All Payrolls (Paginated)
    @Operation(summary = "Fetch all payrolls (paginated)")
    @GetMapping("all")
    public ResponseEntity<ApiResponse<PagedResponse<PayrollDto>>> getAllPayrolls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<PayrollDto> response = payrollService.getAll(page, size);
        return ResponseEntity.ok(ApiResponse.success("Payroll list fetched", response));
    }

    // Get Payroll By Id
    @Operation(summary = "Get payroll by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PayrollDto>> getPayrollById(@PathVariable Long id) {
        PayrollDto payroll = payrollService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Payroll fetched by id", payroll));
    }

    // Update Payroll
    @Operation(summary = "Update payroll by ID")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<PayrollDto>> updatePayroll(@PathVariable Long id, @RequestBody PayrollDto payrollDto) {
        PayrollDto updated = payrollService.update(id, payrollDto);
        return ResponseEntity.ok(ApiResponse.success("Payroll updated successfully", updated));
    }

    // Delete Payroll
    @Operation(summary = "Delete payroll by ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deletePayroll(@PathVariable Long id) {
        payrollService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Payroll deleted successfully", null));
    }

    // status update
    @Operation(summary = "Update payroll status")
    @PutMapping("/status/{id}")
    public ResponseEntity<PayrollDto> updateStatus(
            @PathVariable Long id,
            @RequestParam Status status) {

        PayrollDto updatedDto = payrollService.updateStatus(id, status);
        return ResponseEntity.ok(updatedDto);
    }



}
