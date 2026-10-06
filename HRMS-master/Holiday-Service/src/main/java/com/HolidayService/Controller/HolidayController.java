package com.HolidayService.Controller;

import com.HolidayService.Dto.HolidayRequestDto;
import com.HolidayService.Dto.HolidayResponseDto;
import com.HolidayService.Service.HolidayService;
import com.HolidayService.common.ApiResponse;
import com.HolidayService.common.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/holidays")
@Tag(name = "Holiday Controller", description = "Handles CRUD operations and searching for holidays")
public class HolidayController {
    @Autowired
    private HolidayService holidayService;

    @Operation(summary = "Create a new holiday")
    @PostMapping("create")
    public ResponseEntity<ApiResponse<HolidayResponseDto>> create(@RequestBody HolidayRequestDto dto) {
        HolidayResponseDto created = holidayService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true,"Holiday created successfully",  created));
    }

    // Get all with pagination
    @Operation(summary = "Get all holidays (paginated)")
    @GetMapping("all")
    public ResponseEntity<ApiResponse<PagedResponse<HolidayResponseDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PagedResponse<HolidayResponseDto> response = holidayService.getAll(page, size);
        return ResponseEntity.ok(new ApiResponse<>(true,"Fetched all holidays",  response));
    }

    // Get by ID
    @Operation(summary = "Get a holiday by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HolidayResponseDto>> getById(@PathVariable Long id) {
        HolidayResponseDto response = holidayService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(true,"Holiday found",  response));
    }

    // Update
    @Operation(summary = "Update a holiday by ID")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<HolidayResponseDto>> update(@PathVariable Long id, @RequestBody HolidayRequestDto dto) {
        HolidayResponseDto updated = holidayService.update(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true,"Holiday updated",  updated));
    }

    // Delete
    @Operation(summary = "Delete a holiday by ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        holidayService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true,"Holiday deleted",  null));
    }

    // Search by title
    @Operation(summary = "Search a holiday by title")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<HolidayResponseDto>> searchByTitle(@RequestParam String title) {
        HolidayResponseDto result = holidayService.searchByTitle(title);
        return ResponseEntity.ok(new ApiResponse<>(true,"Holiday found",  result));
    }
    @Operation(summary = "Update action status for a holiday")
    @PatchMapping("/action/{id}")
    public ResponseEntity<ApiResponse<HolidayResponseDto>> updateAction(
            @PathVariable Long id,
            @RequestParam String action) {

        HolidayResponseDto response = holidayService.updateAction(id, action);
        return ResponseEntity.ok(new ApiResponse<>(true,"Action updated successfully",  response));
    }

}
