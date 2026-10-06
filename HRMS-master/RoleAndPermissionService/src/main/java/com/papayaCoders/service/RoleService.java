package com.papayaCoders.service;

import com.papayaCoders.FeignClients.*;
import com.papayaCoders.common.ApiResponse;
import com.papayaCoders.common.PagedResponse;
import com.papayaCoders.dto.*;

import com.papayaCoders.exception.ResourceNotFoundException;
import com.papayaCoders.model.Permission;
import com.papayaCoders.model.Role;
import com.papayaCoders.reposiory.PermissionRepository;
import com.papayaCoders.reposiory.RoleRepository;

import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private LeaveServiceClient leaveServiceClient;

    @Autowired
    private ExpenseServiceClient expenseServiceClient;

    @Autowired
    private PayrollServiceClient payrollServiceClient;

    @Autowired
    private HolidaysServiceClient holidaysServiceClient;

    @Autowired
    private EventServiceClient eventServiceClient;

    @Autowired
    private LeadServiceClient leadServiceClient;

    @Autowired
    private NoticeServiceClient noticeServiceClient;

    @Autowired
    private ClientServiceClient clientServiceClient;

    @Autowired
    private CouponServiceClient couponServiceClient;

    @Autowired
    private PlanServiceClient planServiceClient;

    @Autowired
    private PurchseServiceClient purchseServiceClient;
    // create role

    @Transactional
    public ApiResponse<RoleResponseDto> create(RoleDto roleDto) {
        List<Permission> permissions = new ArrayList<>();

        // Check if permission IDs are provided
        if (roleDto.getPermissionIds() != null && !roleDto.getPermissionIds().isEmpty()) {
            // Validate that all permissions associated with the role exist in the user service
            List<Long> permissionIds = roleDto.getPermissionIds();
            permissions = permissionRepository.findAllById(permissionIds);

            if (permissions.size() != permissionIds.size()) {
                return new ApiResponse<>(false, "One or more permissions are invalid.");
            }

            // Validate each permission with user-service API routes
            for (Permission permission : permissions) {
                boolean validPermission = isPermissionValid(permission);
                if (!validPermission) {
                    return new ApiResponse<>(false, "Permission " + permission.getName() + " is invalid or unavailable.");
                }
            }
        }

        // Convert and format role name
        String formattedRoleName = "ROLE_" + roleDto.getRoleName().toUpperCase();

        // 🔒 Check if role already exists
        if (roleRepository.existsByRoleName(formattedRoleName)) {
            return new ApiResponse<>(false, "Role with name '" + formattedRoleName + "' already exists.");
        }

        // Create role and associate permissions
        Role role = new Role();
        role.setRoleName(formattedRoleName);
        role.setPermissions(permissions);
        role.setCreatedAt(LocalDate.now());
        role.setUpdatedAt(LocalDate.now());

        Role save = roleRepository.save(role);
        RoleResponseDto roleResponseDto = mapper.map(save, RoleResponseDto.class);

        return new ApiResponse<>(true, "Role created successfully", roleResponseDto);
    }


    public boolean isPermissionValid(Permission permission) {
        List<RouteDto> userRoutes = userServiceClient.getRoutes();
        List<RouteDto> leaveRoutes = leaveServiceClient.getRoutes();
        List<RouteDto> expenseRoutes = expenseServiceClient.getRoutes();
        List<RouteDto> payrollRoutes = payrollServiceClient.getRoutes();
        List<RouteDto> holidaysRoutes = holidaysServiceClient.getRoutes();
        List<RouteDto> eventRoutes = eventServiceClient.getRoutes();
        List<RouteDto> leadRoutes = leadServiceClient.getRoutes();
        List<RouteDto> noticeRoutes = noticeServiceClient.getRoutes();
        List<RouteDto> clientRoutes = clientServiceClient.getRoutes();
        List<RouteDto> couponRoutes = couponServiceClient.getRoutes();
        List<RouteDto> planRoutes = planServiceClient.getRoutes();
        List<RouteDto> purchaseRoutes = purchseServiceClient.getRoutes();


        List<RouteDto> allRoutes = new ArrayList<>();
        allRoutes.addAll(userRoutes);
        allRoutes.addAll(leaveRoutes);
        allRoutes.addAll(expenseRoutes);
        allRoutes.addAll(payrollRoutes);
        allRoutes.addAll(holidaysRoutes);
        allRoutes.addAll(eventRoutes);
        allRoutes.addAll(leadRoutes);
        allRoutes.addAll(noticeRoutes);
        allRoutes.addAll(clientRoutes);
        allRoutes.addAll(couponRoutes);
        allRoutes.addAll(planRoutes);
        allRoutes.addAll(purchaseRoutes);

        String route = permission.getApiRoute().startsWith("/") ? permission.getApiRoute() : "/" + permission.getApiRoute();

        return allRoutes.stream().anyMatch(r ->
                r.getMethod().equalsIgnoreCase(permission.getHttpMethod()) &&
                        r.getPath().equalsIgnoreCase(route)
        );
    }

    // update Role
    public ApiResponse<RoleDto> update(Long id, RoleDto roleDto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        if (roleDto.getRoleName() != null) {
            String cleanedName = roleDto.getRoleName().toUpperCase().trim();

            // Ensure it starts with ROLE_
            if (!cleanedName.startsWith("ROLE_")) {
                cleanedName = "ROLE_" + cleanedName;
            }

            if (!cleanedName.equalsIgnoreCase(role.getRoleName())) {
                boolean exists = roleRepository.existsByRoleNameIgnoreCaseAndIdNot(cleanedName, id);
                if (exists) {
                    return new ApiResponse<>(false, "Role name already exists", null);
                }
                role.setRoleName(cleanedName);
            }
        }

        if (roleDto.getPermissionIds() != null && !roleDto.getPermissionIds().isEmpty()) {
            List<Permission> newPermissions = permissionRepository.findAllById(roleDto.getPermissionIds());

            if (newPermissions.size() != roleDto.getPermissionIds().size()) {
                List<Long> foundIds = newPermissions.stream().map(Permission::getId).toList();
                List<Long> invalidIds = roleDto.getPermissionIds().stream()
                        .filter(idVal -> !foundIds.contains(idVal))
                        .toList();
                return new ApiResponse<>(false, "Invalid permission IDs: " + invalidIds, null);
            }

            role.setPermissions(newPermissions); // Replacing permissions
        }

        role.setUpdatedAt(LocalDate.now());
        Role updatedRole = roleRepository.save(role);

        RoleDto updatedDto = new RoleDto();
        updatedDto.setId(updatedRole.getId());
        updatedDto.setRoleName(updatedRole.getRoleName());
        updatedDto.setPermissionIds(updatedRole.getPermissions().stream().map(Permission::getId).toList());

        return new ApiResponse<>(true, "Role updated successfully", updatedDto);
    }

    // delete Role
    public ApiResponse delete(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        roleRepository.delete(role);
        return new ApiResponse<>(true, "Role is deleted");
    }

    // get All role
    public PagedResponse<RoleAllResponseDto> getAllRole(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Role> roles = roleRepository.findAll(pageable);

        List<RoleAllResponseDto> roleDtos = roles.getContent().stream()
                .map(role -> {
                    RoleAllResponseDto dto = new RoleAllResponseDto();
                    dto.setId(role.getId());
                    dto.setRoleName(role.getRoleName());

                    // Manually map each permission
                    List<PermissionDto> permissionDtos = role.getPermissions().stream()
                            .map(p -> {
                                PermissionDto permissionDto = new PermissionDto();
                                permissionDto.setId(String.valueOf(p.getId()));
                                permissionDto.setName(p.getName());
                                permissionDto.setStatus(p.getStatus());
                                permissionDto.setCreatedAt(p.getCreatedAt());
                                permissionDto.setUpdatedAt(p.getUpdatedAt());
                                permissionDto.setHttpMethod(p.getHttpMethod());
                                permissionDto.setApiRoute(p.getApiRoute());
                                return permissionDto;
                            })
                            .toList();

                    dto.setPermissions(permissionDtos);
                    return dto;
                })
                .toList();

        return new PagedResponse<>(
                roleDtos,
                roles.getNumber(),
                roles.getSize(),
                roles.getTotalElements(),
                roles.getTotalPages(),
                roles.isLast()
        );
    }

    //Removed permission
    public ApiResponse<RoleResponseDto> removePermissionFromRole(Long roleId, Long permissionId) throws BadRequestException {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with ID: " + permissionId));

        // Remove permission if it exists
        boolean removed = role.getPermissions().removeIf(p -> p.getId().equals(permissionId));

        if (!removed) {
            throw new BadRequestException("Permission not associated with this role");
        }

        role.setUpdatedAt(LocalDate.now());
        Role updatedRole = roleRepository.save(role);
        RoleResponseDto responseDto = mapper.map(updatedRole, RoleResponseDto.class);

        return new ApiResponse<>(true, "Permission removed from role", responseDto);
    }

    //get role by id
    public ApiResponse<RoleDto> getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role Not Found"));

        RoleDto roleDto = new RoleDto();
        roleDto.setId(role.getId());
        roleDto.setRoleName(role.getRoleName());

        // Convert List<Permission> to List<Long> (permission IDs)
        List<Long> permissionIds = role.getPermissions()
                .stream()
                .map(Permission::getId)
                .collect(Collectors.toList());

        roleDto.setPermissionIds(permissionIds);

        return new ApiResponse<>(true, "Role Fetched successfully", roleDto);
    }

    public ApiResponse<RoleResponseDto> assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if (optionalRole.isEmpty()) {
            return new ApiResponse<>(false, "Role not found with ID: " + roleId);
        }

        Role role = optionalRole.get();

        // Validate permissions exist in DB
        List<Permission> permissionsToAdd = permissionRepository.findAllById(permissionIds);
        if (permissionsToAdd.size() != permissionIds.size()) {
            return new ApiResponse<>(false, "One or more permissions are invalid.");
        }

        // Validate each permission with user-service API routes
        for (Permission permission : permissionsToAdd) {
            boolean validPermission = isPermissionValid(permission);
            if (!validPermission) {
                return new ApiResponse<>(false, "Permission " + permission.getName() + " is invalid or unavailable.");
            }
        }


        Set<Permission> existingPermissions = new HashSet<>(role.getPermissions());
        existingPermissions.addAll(permissionsToAdd);
        role.setPermissions(new ArrayList<>(existingPermissions));

        role.setUpdatedAt(LocalDate.now());
        Role updatedRole = roleRepository.save(role);

        RoleResponseDto responseDto = mapper.map(updatedRole, RoleResponseDto.class);
        return new ApiResponse<>(true, "Permissions assigned successfully to role", responseDto);
    }

}






