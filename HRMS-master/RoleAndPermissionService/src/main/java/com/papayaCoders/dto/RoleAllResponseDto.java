package com.papayaCoders.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleAllResponseDto
{
    private Long id;
    private String roleName;
    private List<PermissionDto> permissions;
}
