package com.coupon.Controller;

import com.coupon.Config.RoutesConfig;
import com.coupon.Dto.RouteDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/coupon/routes")
@Tag(name = "Coupon Routes API", description = "Provides configured route details for Coupon Service")
public class RouteController {

    @Autowired
    private RoutesConfig routesConfig;

    @Operation(summary = "Get all Coupon Routes", description = "Fetches the list of all configured routes for Coupon module.")
    @GetMapping
    public List<RouteDto> getRoutes() {
        return routesConfig.getRoutes();
    }
}
