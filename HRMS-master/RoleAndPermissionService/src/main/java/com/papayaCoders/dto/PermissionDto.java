package com.papayaCoders.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PermissionDto
{
    private String id;

    @NotBlank(message = "Message name is required")
    private String name;

    private String status="ACTIVE";

    private LocalDate createdAt;

    private LocalDate updatedAt;
    private String httpMethod; // e.g., POST, PUT
    private String apiRoute;

}

