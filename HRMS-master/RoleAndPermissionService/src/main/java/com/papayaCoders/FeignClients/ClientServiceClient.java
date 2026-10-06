package com.papayaCoders.FeignClients;

import com.papayaCoders.Interceptor.FeignClientInterceptor;
import com.papayaCoders.dto.RouteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(
        name = "CLIENT-SERVICE",
        url = "${client.url}",
        configuration = FeignClientInterceptor.class
)
public interface ClientServiceClient {
    @GetMapping("/api/clients/routes")
    List<RouteDto> getRoutes();
}
