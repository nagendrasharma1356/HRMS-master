package com.papaya.notice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.papaya.notice.Common.ApiResponse;
import com.papaya.notice.Common.PageResponse;
import com.papaya.notice.dto.EmployeeNoticeDto;
import com.papaya.notice.service.EmployeeNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/employee/notice")
public class EmployeeNoticeController {

    @Autowired
    private EmployeeNoticeService employeeNoticeService;

    @Operation(summary = "Add a new employee notice")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<EmployeeNoticeDto>> addNotice(
            @RequestPart("notice") String noticeJson,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        EmployeeNoticeDto dto = new ObjectMapper().readValue(noticeJson, EmployeeNoticeDto.class);
        EmployeeNoticeDto savedDto = employeeNoticeService.addNotice(dto, file);
        return ResponseEntity.ok(new ApiResponse<>(true, "Notice added", savedDto));
    }

    @Operation(summary = "Get all employee notices with optional filters")
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<PageResponse<EmployeeNoticeDto>>> getAllNoticesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        PageResponse<EmployeeNoticeDto> pageResponse = employeeNoticeService.getAllNoticesPaginated(page, size, date, month, year, from, to);
        return ResponseEntity.ok(new ApiResponse<>(true, "Employee Notices fetched successfully", pageResponse));
    }

    @Operation(summary = "Get employee notice by ID")
    @GetMapping("getById/{id}")
    public ResponseEntity<ApiResponse<EmployeeNoticeDto>> getById(@PathVariable Long id) {
        EmployeeNoticeDto dto = employeeNoticeService.getNoticeById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Employee notice found", dto));
    }

    @Operation(summary = "Delete employee notice by ID")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        employeeNoticeService.deleteNotice(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Employee notice deleted", null));
    }

    @Operation(summary = "Search employee notices by heading")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<EmployeeNoticeDto>>> searchByHeading(@RequestParam String heading) {
        List<EmployeeNoticeDto> list = employeeNoticeService.searchByHeading(heading);
        return ResponseEntity.ok(new ApiResponse<>(true, "Search results", list));
    }

    @Operation(summary = "Update employee notice by ID")
    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<EmployeeNoticeDto>> update(@PathVariable Long id, @RequestBody EmployeeNoticeDto dto) {
        EmployeeNoticeDto updated = employeeNoticeService.updateNotice(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Employee notice updated", updated));
    }
}
