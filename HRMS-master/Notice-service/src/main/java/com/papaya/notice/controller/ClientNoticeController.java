package com.papaya.notice.controller;

import com.papaya.notice.Common.ApiResponse;
import com.papaya.notice.Common.PageResponse;
import com.papaya.notice.dto.ClientNoticeDto;
import com.papaya.notice.service.ClientNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/client")
@Tag(name = "Client Notice APIs", description = "CRUD operations and search for Client Notices")
public class ClientNoticeController {

    @Autowired
    private ClientNoticeService clientNoticeService;

    @Operation(
            summary = "Add a new Client Notice with optional file upload",
            requestBody = @RequestBody(
                    description = "ClientNoticeDto JSON and optional file",
                    content = {
                            @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
                    }
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Client Notice added successfully",
                            content = @Content(schema = @Schema(implementation = ClientNoticeDto.class))
                    )
            }
    )
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ClientNoticeDto> addClientNotice(
            @ModelAttribute ClientNoticeDto dto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        ClientNoticeDto created = clientNoticeService.addNotice(dto, file);
        return new ApiResponse<>(true, "Client Notice added successfully", created);
    }

    @Operation(
            summary = "Get paginated list of Client Notices with optional filters",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Client Notices fetched successfully",
                            content = @Content(schema = @Schema(implementation = PageResponse.class))
                    )
            }
    )
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<PageResponse<ClientNoticeDto>>> getAllNoticesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        PageResponse<ClientNoticeDto> pageResponse = clientNoticeService.getAllNoticesPaginated(page, size, date, month, year, from, to);
        return ResponseEntity.ok(new ApiResponse<>(true, "Client Notices fetched successfully", pageResponse));
    }

    @Operation(
            summary = "Get Client Notice by ID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Client Notice found",
                            content = @Content(schema = @Schema(implementation = ClientNoticeDto.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Client Notice not found"
                    )
            }
    )
    @GetMapping("getById/{id}")
    public ResponseEntity<ApiResponse<ClientNoticeDto>> getById(@PathVariable Long id) {
        ClientNoticeDto dto = clientNoticeService.getNoticeById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Client notice found", dto));
    }

    @Operation(
            summary = "Delete Client Notice by ID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Client Notice deleted successfully"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Client Notice not found"
                    )
            }
    )
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        clientNoticeService.deleteNotice(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Client notice deleted", null));
    }

    @Operation(
            summary = "Search Client Notices by heading text",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Search results returned",
                            content = @Content(schema = @Schema(implementation = ClientNoticeDto.class))
                    )
            }
    )
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ClientNoticeDto>>> searchByHeading(@RequestParam String heading) {
        List<ClientNoticeDto> list = clientNoticeService.searchByHeading(heading);
        return ResponseEntity.ok(new ApiResponse<>(true, "Search results", list));
    }

    @Operation(
            summary = "Update Client Notice by ID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Client Notice updated successfully",
                            content = @Content(schema = @Schema(implementation = ClientNoticeDto.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Client Notice not found"
                    )
            }
    )
    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<ClientNoticeDto>> update(@PathVariable Long id, @RequestBody ClientNoticeDto dto) {
        ClientNoticeDto updated = clientNoticeService.updateNotice(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Client notice updated", updated));
    }
}
