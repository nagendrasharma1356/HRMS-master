package com.papayaCoders.FeignClients;

import com.papayaCoders.Interceptor.FeignClientInterceptor;
import com.papayaCoders.dto.RouteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "COUPON-SERVICE" ,url = "${coupon.url}",configuration = FeignClientInterceptor.class)
public interface CouponServiceClient {
    @GetMapping("/api/coupon/routes")
    List<RouteDto> getRoutes();
}
