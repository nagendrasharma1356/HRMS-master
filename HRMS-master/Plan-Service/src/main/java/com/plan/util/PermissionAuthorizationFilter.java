package com.plan.util;


import com.plan.Dto.PermissionDto;
import com.plan.FeignClient.RoleServiceClient;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class PermissionAuthorizationFilter extends OncePerRequestFilter {
    @Autowired
    private RoleServiceClient roleServiceClient;
    @Autowired
    private JwtHelper jwtHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // Skip public endpoints
        if (path.equals("/api/plan/routes")||
                path.startsWith("/v3/api-docs") ||
                path.equals("/swagger-ui.html") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/webjars")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = jwtHelper.getClaimsFromToken(token);
            String role = jwtHelper.getRoleFromToken(token);

            // Fetch permissions for the role from RoleServiceClient
            List<PermissionDto> permissions = roleServiceClient.getPermissionsByRole(role);

            // Normalize the path (if needed)
            String normalizedPath = path.startsWith("/") ? path : "/" + path;

            List<PermissionDto> permissionss = roleServiceClient.getPermissionsByRole(role);

// Check if the current route is protected (i.e., listed in permissions DB)
            boolean isProtectedRoute = permissionss.stream()
                    .anyMatch(p -> p.getApiRoute().equalsIgnoreCase(normalizedPath));

// If route is protected, check if the role has permission
            if (isProtectedRoute) {
                boolean hasPermission = permissionss.stream()
                        .anyMatch(p -> p.getApiRoute().equalsIgnoreCase(normalizedPath)
                                && p.getHttpMethod().equalsIgnoreCase(method));

                if (!hasPermission) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Access Denied: You do not have permission to perform this operation.");
                    return;
                }
            }

            // If permission exists, continue filter chain
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
        }
    }


}

