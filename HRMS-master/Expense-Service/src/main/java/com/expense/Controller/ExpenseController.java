package com.expense.Controller;

import com.expense.common.ApiResponse;
import com.expense.common.PagedResponse;
import com.expense.dto.ExpenseDto;
import com.expense.service.ExpenseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Tag(name = "Expense Controller", description = "Manage expense operations such as create, read, update, and delete")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Operation(summary = "Create a new expense")
    @PostMapping("create")
    public ResponseEntity<ApiResponse<ExpenseDto>> create(@RequestBody ExpenseDto dto) throws BadRequestException {
        ExpenseDto created = expenseService.createExpense(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Expense created successfully", created));
    }

    @Operation(summary = "Fetch all expenses with pagination")
    @GetMapping("all")
    public ResponseEntity<ApiResponse<PagedResponse<ExpenseDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<ExpenseDto> all = expenseService.getAll(page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "Expenses fetched successfully", all));
    }

    @Operation(summary = "Get expense details by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseDto>> getById(@PathVariable Long id) {
        ExpenseDto dto = expenseService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Expense fetched successfully", dto));
    }

    @Operation(summary = "Update an expense by ID")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<ExpenseDto>> update(@PathVariable Long id, @RequestBody ExpenseDto dto) {
        ExpenseDto updated = expenseService.update(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Expense updated successfully", updated));
    }

    @Operation(summary = "Delete an expense by ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        expenseService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Expense deleted successfully", null));
    }
}
