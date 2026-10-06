package com.example.Client.Service.controller;




import com.example.Client.Service.Config.RoutesConfig;
import com.example.Client.Service.dto.RouteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clients/routes")
public class RouteController
{
    @Autowired
    private RoutesConfig routesConfig;

    @GetMapping
    public List<RouteDto> getRoutes() {
        return routesConfig.getRoutes();
    }
}
