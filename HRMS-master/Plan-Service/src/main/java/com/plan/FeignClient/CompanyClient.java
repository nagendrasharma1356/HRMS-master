package com.plan.FeignClient;

import com.plan.FeignClientDto.CompanyDto;
import com.plan.Interceptor.FeignClientInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CLIENT-SERVICE",url = "${company.url}",configuration = FeignClientInterceptor.class)
public interface CompanyClient {

    @GetMapping("/api/companies/getById/{id}")
    CompanyDto getCompanyById(@PathVariable("id") Long companyId);
}
