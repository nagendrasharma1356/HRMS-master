package com.papaya.lead.service;

import com.papaya.lead.config.PageResponse;
import com.papaya.lead.dto.CompanyDetailsDTO;

import java.util.List;

public interface CompanyDetailsService {

    CompanyDetailsDTO createCompanyDetails(CompanyDetailsDTO dto);

    CompanyDetailsDTO getCompanyDetailsById(Long id);

    List<CompanyDetailsDTO> getAllCompanyDetails();

    CompanyDetailsDTO updateCompanyDetails(Long id, CompanyDetailsDTO dto);

    void deleteCompanyDetails(Long id);

    PageResponse<CompanyDetailsDTO> getAllCompaniesPaginated(int page, int size);

    //Create company and assign to a specific lead
    CompanyDetailsDTO createCompanyForLead(Long leadId, CompanyDetailsDTO dto);
}