package com.planPurchase.FeignCilent;

import com.planPurchase.Common.ApiResponse;
import com.planPurchase.FeignClientDto.CompanyServiceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PLAN-SERVICE", url = "${plan.url}")
public interface CompanyServiceClient {

    @GetMapping("/api/companyServices/{id}")
    ApiResponse<CompanyServiceDto> getServiceById(@PathVariable("id") Long id);

}
