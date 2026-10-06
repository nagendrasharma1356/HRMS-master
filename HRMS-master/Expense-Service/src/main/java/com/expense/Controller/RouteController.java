package com.expense.Controller;


import com.expense.config.RoutesConfig;
import com.expense.dto.RouteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/expense/routes")
public class RouteController
{
    @Autowired
    private RoutesConfig routesConfig;

    @GetMapping
    public List<RouteDto> getRoutes() {
        return routesConfig.getRoutes();
    }
}
