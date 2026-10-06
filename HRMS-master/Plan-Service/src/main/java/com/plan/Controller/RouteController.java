package com.plan.Controller;

import com.plan.Dto.RouteDto;
import com.plan.config.RoutesConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/plan/routes")
@Tag(name = "Plan Routes API", description = "Provides configured route details for Plan Service")
public class RouteController {

    @Autowired
    private RoutesConfig routesConfig;

    @Operation(summary = "Get all Plan Routes", description = "Fetches the list of all configured routes for Plan module.")
    @GetMapping
    public List<RouteDto> getRoutes() {
        return routesConfig.getRoutes();
    }
}
