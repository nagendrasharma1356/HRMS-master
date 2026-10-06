package com.papayaCoders.FeignClients;

import com.papayaCoders.Interceptor.FeignClientInterceptor;
import com.papayaCoders.dto.RouteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "LEAD-SERVICE",url = "${lead.url}" ,configuration = FeignClientInterceptor.class)
public interface LeadServiceClient {
    @GetMapping("/api/leads/routes")
    List<RouteDto> getRoutes();
}
