package com.papayaCoders.dto;

import lombok.Data;

import java.util.List;

@Data
public class PermissionAssignmentRequest
{
    private List<Long> permissionIds;
}
