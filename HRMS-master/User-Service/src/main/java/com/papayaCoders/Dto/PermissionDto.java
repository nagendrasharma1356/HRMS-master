package com.papayaCoders.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PermissionDto
{
    private Long id;
    private String name;
    private String httpMethod;
    private String apiRoute;
    private String roleName;

}
