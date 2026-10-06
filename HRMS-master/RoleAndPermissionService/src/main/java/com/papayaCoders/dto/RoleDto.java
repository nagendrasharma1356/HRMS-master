package com.papayaCoders.dto;

import com.papayaCoders.model.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleDto
{
    private Long id;
    private String roleName;
    private List<Long> permissionIds;


}
