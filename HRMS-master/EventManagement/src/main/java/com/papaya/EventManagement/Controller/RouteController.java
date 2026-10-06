package com.papaya.EventManagement.Controller;


import com.papaya.EventManagement.DTO.RouteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/event/routes")
public class RouteController
{
    @Autowired
    private com.papaya.EventManagement.Config.RoutesConfig routesConfig;

    @GetMapping
    public List<RouteDto> getRoutes() {
        return routesConfig.getRoutes();
    }
}
