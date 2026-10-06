package com.example.Client.Service.service;

import com.example.Client.Service.common.PagedResponse;
import com.example.Client.Service.dto.ClientDto;
import com.example.Client.Service.dto.CompanyDto;

import java.util.List;

public interface CompanyService {


    CompanyDto createCompany(CompanyDto companyDto);
    PagedResponse<CompanyDto> getAllCompanies(int page, int size);

    CompanyDto getCompanyById(Long id);
    CompanyDto assignClientsToCompany(Long companyId, List<Long> clientIds);
    // ✅ 4. Update Company
    CompanyDto updateCompany(Long id, CompanyDto companyDto);

    // ✅ 5. Delete Company
    void deleteCompany(Long id);
}
