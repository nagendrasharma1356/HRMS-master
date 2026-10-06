package com.papayaCoders.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionDto {
    private Long id;
    private String name;
    private String httpMethod;
    private String apiRoute;
}
