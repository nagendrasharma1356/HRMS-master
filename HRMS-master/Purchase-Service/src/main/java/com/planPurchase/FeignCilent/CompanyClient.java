package com.planPurchase.FeignCilent;

import com.planPurchase.FeignClientDto.CompanyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-service", url = "${company.url}")
public interface CompanyClient {

    @GetMapping("/api/companies/getById/{id}")
    CompanyDto getCompanyById(@PathVariable("id") Long companyId);
}
