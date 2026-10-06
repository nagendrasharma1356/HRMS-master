package com.leave_management.controller;

import com.leave_management.Config.RoutesConfig;
import com.leave_management.Dto.RouteDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaves/routes")
public class RouteController
{
    @Autowired
    private RoutesConfig routesConfig;

    @GetMapping
    public List<RouteDto> getRoutes() {
        return routesConfig.getRoutes();
    }
}
