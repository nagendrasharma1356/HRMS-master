package com.papayaCoders.FeignClients;

import com.papayaCoders.Interceptor.FeignClientInterceptor;
import com.papayaCoders.dto.RouteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "HOLIDAY-SERVICE",url = "${holiday.url}" ,configuration = FeignClientInterceptor.class)
public interface  HolidaysServiceClient {
    @GetMapping("/api/holidays/routes")
    List<RouteDto> getRoutes();
}
