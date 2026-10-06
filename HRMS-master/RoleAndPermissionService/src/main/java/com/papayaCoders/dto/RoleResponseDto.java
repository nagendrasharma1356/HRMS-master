package com.papayaCoders.dto;

import com.papayaCoders.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleResponseDto
{
    private Long id;
    private String roleName;

    public RoleResponseDto(Role savedRole) {
    }
}
