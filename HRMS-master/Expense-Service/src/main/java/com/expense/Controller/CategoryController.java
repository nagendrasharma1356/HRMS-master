package com.expense.Controller;

import com.expense.common.ApiResponse;
import com.expense.common.PagedResponse;
import com.expense.dto.ExpenseCategoryDto;
import com.expense.service.ExpenseCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@Tag(name = "Expense Category Controller", description = "Manage expense categories (CRUD operations)")
public class CategoryController {

    @Autowired
    private ExpenseCategoryService categoryService;

    @Operation(summary = "Create a new expense category")
    @PostMapping("create")
    public ResponseEntity<ApiResponse<ExpenseCategoryDto>> create(@RequestBody ExpenseCategoryDto categoryDto) {
        try {
            ExpenseCategoryDto expenseCategoryDto = categoryService.create(categoryDto);
            ApiResponse<ExpenseCategoryDto> response = new ApiResponse<>(true, "Category created successfully", expenseCategoryDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<ExpenseCategoryDto> response = new ApiResponse<>(false, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Fetch all expense categories with pagination")
    @GetMapping("all")
    public ResponseEntity<PagedResponse<ExpenseCategoryDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<ExpenseCategoryDto> all = categoryService.getAll(page, size);
        return ResponseEntity.ok(all);
    }

    @Operation(summary = "Get a category by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseCategoryDto>> getById(@PathVariable Long id) {
        try {
            ExpenseCategoryDto byId = categoryService.getById(id);
            ApiResponse<ExpenseCategoryDto> response = new ApiResponse<>(true, "Fetched successfully", byId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<ExpenseCategoryDto> response = new ApiResponse<>(false, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Update an expense category by ID")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<ExpenseCategoryDto>> update(@PathVariable Long id, @RequestBody ExpenseCategoryDto categoryDto) {
        ExpenseCategoryDto update = categoryService.update(id, categoryDto);
        ApiResponse<ExpenseCategoryDto> response = new ApiResponse<>(true, "Category updated successfully", update);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete an expense category by ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        categoryService.delete(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Category deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}
