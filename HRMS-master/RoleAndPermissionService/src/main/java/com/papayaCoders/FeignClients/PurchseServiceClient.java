package com.papayaCoders.FeignClients;

import com.papayaCoders.Interceptor.FeignClientInterceptor;
import com.papayaCoders.dto.RouteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "PURCHASE-SERVICE" ,url = "${purchase.url}",configuration = FeignClientInterceptor.class)
public interface PurchseServiceClient {
    @GetMapping("/api/purchase/routes")
    List<RouteDto> getRoutes();
}
