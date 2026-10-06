package com.payroll.FeignClientService;

import com.payroll.Dto.MonthlySummaryDto;
import com.payroll.Dto.UserDto;
import com.payroll.common.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "USER-SERVICE",url = "${user.url}")
public interface UserClient {

    @GetMapping("/api/common/users/{role}/{id}")
    UserDto getUserByIdAndRole(@PathVariable("role") String role, @PathVariable("id") Long id);

    @GetMapping("/api/common/monthlySummary")
    ApiResponse<MonthlySummaryDto> getMonthlyLeaveSalarySummary(@RequestParam("userId") Long userId, @RequestParam("role") String role);


}
