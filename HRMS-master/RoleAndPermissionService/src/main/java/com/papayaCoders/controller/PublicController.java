package com.papayaCoders.controller;

import com.papayaCoders.common.ApiResponse;
import com.papayaCoders.dto.PermissionAssignmentRequest;
import com.papayaCoders.dto.PermissionDto;
import com.papayaCoders.dto.RoleResponseDto;
import com.papayaCoders.exception.ResourceNotFoundException;
import com.papayaCoders.model.Role;
import com.papayaCoders.reposiory.RoleRepository;
import com.papayaCoders.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController
{
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper mapper;

    @GetMapping("/{roleName}/permissions")
    public ResponseEntity<List<PermissionDto>> getPermissionsByRole(@PathVariable String roleName) {
        Role role = roleRepository.findByRoleNameIgnoreCase(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));

        List<PermissionDto> dtoList = role.getPermissions().stream()
                .map(permission -> mapper.map(permission, PermissionDto.class))
                .toList();

        return ResponseEntity.ok(dtoList);
    }


}
