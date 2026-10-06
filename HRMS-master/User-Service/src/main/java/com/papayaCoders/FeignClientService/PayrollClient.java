package com.papayaCoders.FeignClientService;

import com.papayaCoders.Dto.PayrollDto;
import com.papayaCoders.Interceptor.FeignClientInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "PAYROLL-SERVICE",url = "${payroll.url}",configuration = FeignClientInterceptor.class)
public interface PayrollClient {

    @PutMapping("/payrolls/{payrollId}/assign/{userId}")
    PayrollDto assignPayrollToUser(@PathVariable("payrollId") Long payrollId,
                                   @PathVariable("userId") Long userId);
}