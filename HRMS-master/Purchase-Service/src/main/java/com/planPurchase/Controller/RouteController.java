package com.planPurchase.Controller;

import com.planPurchase.Dto.RouteDto;
import com.planPurchase.config.RoutesConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/purchase/routes")
@Tag(name = "Purchase Routes API", description = "Provides configured route details for Plan Purchase module.")
public class RouteController {

    @Autowired
    private RoutesConfig routesConfig;

    @Operation(summary = "Get all Purchase Routes", description = "Fetches the list of all configured routes for Plan Purchase service.")
    @GetMapping
    public List<RouteDto> getRoutes() {
        return routesConfig.getRoutes();
    }
}
