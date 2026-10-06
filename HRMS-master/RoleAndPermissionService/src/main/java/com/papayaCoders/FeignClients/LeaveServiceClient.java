package com.papayaCoders.FeignClients;

import com.papayaCoders.Interceptor.FeignClientInterceptor;
import com.papayaCoders.dto.RouteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
@FeignClient(name = "LEAVE-SERVICE",url = "${leaves.url}" ,configuration = FeignClientInterceptor.class)
public interface LeaveServiceClient {
    @GetMapping("/api/leaves/routes")
    List<RouteDto> getRoutes();
}
