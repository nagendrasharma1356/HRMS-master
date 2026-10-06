package com.papayaCoders.FeignClientService;

import com.papayaCoders.Interceptor.FeignClientInterceptor;
import com.papayaCoders.common.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "LEAVE-SERVICE",url = "${leave.url}",configuration = FeignClientInterceptor.class)
public interface LeaveClient
{
    @GetMapping("/api/reports/leaves/leaveSummary")
    ApiResponse<Map<String, Object>> getLeaveSummary(@RequestParam("userId") Long userId, @RequestParam("role") String role);

}
