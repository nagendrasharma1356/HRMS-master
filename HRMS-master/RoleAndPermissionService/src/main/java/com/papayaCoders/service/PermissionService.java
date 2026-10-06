package com.papayaCoders.service;

import com.papayaCoders.FeignClients.*;
import com.papayaCoders.common.ApiResponse;
import com.papayaCoders.common.PagedResponse;
import com.papayaCoders.dto.PermissionDto;
import com.papayaCoders.dto.RouteDto;
import com.papayaCoders.exception.ResourceNotFoundException;
import com.papayaCoders.model.Permission;
import com.papayaCoders.reposiory.PermissionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private ModelMapper mapper;

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

    private String normalizeRoute(String route) {
        if (route == null) return null;
        return route.startsWith("/") ? route : "/" + route;
    }

    public ApiResponse<PermissionDto> create(PermissionDto dto) {
        // 🔒 Check if permission with same name already exists (case-insensitive)
        Optional<Permission> existingPermission = permissionRepository.findByNameIgnoreCase(dto.getName());
        if (existingPermission.isPresent()) {
            return new ApiResponse<>(false, "Permission with name '" + dto.getName() + "' already exists.");
        }

        String incomingRoute = normalizeRoute(dto.getApiRoute());

        // 🔒 Check if same route + method already exists
        Optional<Permission> existingRoute = permissionRepository
                .findByApiRouteIgnoreCaseAndHttpMethodIgnoreCase(incomingRoute, dto.getHttpMethod());

        if (existingRoute.isPresent()) {
            return new ApiResponse<>(false, "Permission with route '" + incomingRoute +
                    "' and method '" + dto.getHttpMethod() + "' already exists.");
        }

        // ✅ Validate if route exists in user or leave services
        List<RouteDto> allRoutes = fetchAllRoutes();
        boolean isValidRoute = allRoutes.stream().anyMatch(route ->
                route.getMethod().equalsIgnoreCase(dto.getHttpMethod()) &&
                        route.getPath().equalsIgnoreCase(incomingRoute)
        );

        if (!isValidRoute) {
            return new ApiResponse<>(false, "Invalid route or method — not found.");
        }

        // ✅ Save new permission
        Permission permission = new Permission();
        permission.setName(dto.getName());
        permission.setHttpMethod(dto.getHttpMethod());
        permission.setApiRoute(incomingRoute);
        permission.setCreatedAt(LocalDate.now());
        permission.setUpdatedAt(LocalDate.now());

        Permission saved = permissionRepository.save(permission);
        PermissionDto mapped = mapper.map(saved, PermissionDto.class);

        return new ApiResponse<>(true, "Permission created successfully", mapped);
    }


    public ApiResponse<PermissionDto> update(Long id, PermissionDto dto) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with ID: " + id));

        String newRoute = dto.getApiRoute();
        String newMethod = dto.getHttpMethod();

        // Normalize new route if provided
        String incomingRoute = normalizeRoute(newRoute);

        // Validate route + method combination if either is updated
        if (newRoute != null || newMethod != null) {
            // Use new values if provided, else fallback to existing values for validation
            String methodToCheck = (newMethod != null) ? newMethod : permission.getHttpMethod();
            String routeToCheck = (incomingRoute != null) ? incomingRoute : permission.getApiRoute();

            List<RouteDto> allRoutes = fetchAllRoutes();
            boolean exists = allRoutes.stream().anyMatch(route ->
                    route.getMethod().equalsIgnoreCase(methodToCheck) &&
                            route.getPath().equalsIgnoreCase(routeToCheck)
            );

            if (!exists) {
                return new ApiResponse<>(false, "Invalid route or method — not found in user-service or leave-service");
            }

            // Set updated values
            if (incomingRoute != null) {
                permission.setApiRoute(incomingRoute);
            }
            if (newMethod != null) {
                permission.setHttpMethod(newMethod);
            }
        }

        // Update other fields if provided
        if (dto.getName() != null) {
            permission.setName(dto.getName());
        }

        if (dto.getStatus() != null) {
            permission.setStatus(dto.getStatus());
        }

        permission.setUpdatedAt(LocalDate.now());

        Permission saved = permissionRepository.save(permission);
        PermissionDto mapped = mapper.map(saved, PermissionDto.class);

        return new ApiResponse<>(true, "Permission updated successfully", mapped);
    }

    // get all permission
    public PagedResponse<PermissionDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Permission> permissions = permissionRepository.findAll(pageable);
        List<PermissionDto> dtoList = permissions.stream()
                .map(permission -> mapper.map(permission, PermissionDto.class))
                .collect(Collectors.toList());
        return new PagedResponse<>(
                dtoList,
                permissions.getNumber(),
                permissions.getSize(),
                permissions.getTotalPages(),
                (int) permissions.getTotalElements(),
                permissions.isLast()
        );
    }

    // get permission by id
    public ApiResponse<PermissionDto> getById(Long id) {
        Permission permission = permissionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        PermissionDto permissionDto = mapper.map(permission, PermissionDto.class);
        return new ApiResponse<>(true, "Fetched Successfully", permissionDto);
    }

    // delete permission
    public ApiResponse delete(Long id) {
        Permission permission = permissionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Service not Found"));
        permissionRepository.delete(permission);
        return new ApiResponse(true, "Service Deleted");
    }

    //search by name
    public ApiResponse<PermissionDto> searchByName(String name) {
        Permission permission = permissionRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
        PermissionDto mapped = mapper.map(permission, PermissionDto.class);
        return new ApiResponse<>(true, "Fetched successfully", mapped);

    }

    private List<RouteDto> fetchAllRoutes() {
        List<RouteDto> allRoutes = new ArrayList<>();
        allRoutes.addAll(userServiceClient.getRoutes());
        allRoutes.addAll(leaveServiceClient.getRoutes());
        allRoutes.addAll(expenseServiceClient.getRoutes());
        allRoutes.addAll(payrollServiceClient.getRoutes());
        allRoutes.addAll(holidaysServiceClient.getRoutes());
        allRoutes.addAll(eventServiceClient.getRoutes());
        allRoutes.addAll(leadServiceClient.getRoutes());
        allRoutes.addAll(noticeServiceClient.getRoutes());
        allRoutes.addAll(clientServiceClient.getRoutes());
        allRoutes.addAll(planServiceClient.getRoutes());
        allRoutes.addAll(couponServiceClient.getRoutes());
        allRoutes.addAll(purchseServiceClient.getRoutes());
        return allRoutes;
    }



}
