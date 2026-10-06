package com.papayaCoders.FeignClients;

import com.papayaCoders.Interceptor.FeignClientInterceptor;
import com.papayaCoders.dto.RouteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "PAYROLL-SERVICE" ,url = "${payroll.url}",configuration = FeignClientInterceptor.class)
public interface PayrollServiceClient {
    @GetMapping("/api/payrolls/routes")
    List<RouteDto> getRoutes();
}
