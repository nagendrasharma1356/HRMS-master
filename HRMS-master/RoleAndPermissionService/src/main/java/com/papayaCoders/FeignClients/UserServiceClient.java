package com.papayaCoders.FeignClients;

import com.papayaCoders.Interceptor.FeignClientInterceptor;
import com.papayaCoders.dto.RouteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "USER-SERVICE" ,url = "${user.url}",configuration = FeignClientInterceptor.class)
public interface UserServiceClient
{
    @GetMapping("/api/routes")
    List<RouteDto> getRoutes();
}
