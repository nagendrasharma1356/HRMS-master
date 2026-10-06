package com.papayaCoders.FeignClients;

import com.papayaCoders.Interceptor.FeignClientInterceptor;
import com.papayaCoders.dto.RouteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "EXPENSE-SERVICE" ,url = "${expense.url}",configuration = FeignClientInterceptor.class)
public interface ExpenseServiceClient {
    @GetMapping("/api/expense/routes")
    List<RouteDto> getRoutes();
}
