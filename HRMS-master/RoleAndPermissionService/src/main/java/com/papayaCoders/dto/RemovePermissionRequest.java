package com.papayaCoders.dto;

import lombok.Data;

@Data
public class RemovePermissionRequest {
    private Long roleId;
    private Long permissionId;
}
