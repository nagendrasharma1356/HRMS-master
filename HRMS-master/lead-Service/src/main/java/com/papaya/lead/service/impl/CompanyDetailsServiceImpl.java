package com.papaya.lead.service.impl;

import com.papaya.lead.config.PageResponse;
import com.papaya.lead.dto.CompanyDetailsDTO;
import com.papaya.lead.entity.CompanyDetails;
import com.papaya.lead.entity.LeadContact;
import com.papaya.lead.exception.ResourceNotFoundException;
import com.papaya.lead.mapper.CompanyDetailsMapper;
import com.papaya.lead.repository.CompanyDetailsRepository;
import com.papaya.lead.repository.LeadContactRepository;
import com.papaya.lead.service.CompanyDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyDetailsServiceImpl implements CompanyDetailsService {

    private final CompanyDetailsRepository companyDetailsRepository;
    private final LeadContactRepository leadContactRepository;

    @Override
    public CompanyDetailsDTO createCompanyDetails(CompanyDetailsDTO dto) {
        CompanyDetails entity = CompanyDetailsMapper.toEntity(dto);
        return CompanyDetailsMapper.toDTO(companyDetailsRepository.save(entity));
    }

    @Override
    public CompanyDetailsDTO getCompanyDetailsById(Long id) {
        return companyDetailsRepository.findById(id)
                .map(CompanyDetailsMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("CompanyDetails not found with Id: " + id));
    }

    @Override
    public List<CompanyDetailsDTO> getAllCompanyDetails() {
        return companyDetailsRepository.findAll()
                .stream()
                .map(CompanyDetailsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CompanyDetailsDTO updateCompanyDetails(Long id, CompanyDetailsDTO dto) {
        CompanyDetails existing = companyDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CompanyDetails not found with Id: " + id));

        existing.setCompanyName(dto.getCompanyName());
        existing.setWebsite(dto.getWebsite());
        existing.setMobileNumber(dto.getMobileNumber());
        existing.setOfficePhoneNumber(dto.getOfficePhoneNumber());
        existing.setCountry(dto.getCountry());
        existing.setState(dto.getState());
        existing.setCity(dto.getCity());
        existing.setPostalCode(dto.getPostalCode());
        existing.setAddress(dto.getAddress());

        return CompanyDetailsMapper.toDTO(companyDetailsRepository.save(existing));
    }

    @Override
    public void deleteCompanyDetails(Long id) {
        companyDetailsRepository.deleteById(id);
    }

    @Override
    public PageResponse<CompanyDetailsDTO> getAllCompaniesPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CompanyDetails> companyPage = companyDetailsRepository.findAll(pageable);

        List<CompanyDetailsDTO> content = companyPage.getContent()
                .stream()
                .map(CompanyDetailsMapper::toDTO)
                .collect(Collectors.toList());

        return PageResponse.<CompanyDetailsDTO>builder()
                .content(content)
                .pageNumber(companyPage.getNumber())
                .pageSize(companyPage.getSize())
                .totalElements(companyPage.getTotalElements())
                .totalPages(companyPage.getTotalPages())
                .lastPage(companyPage.isLast())
                .build();
    }

    //Create company and assign to a specific lead
    @Override
    public CompanyDetailsDTO createCompanyForLead(Long leadId, CompanyDetailsDTO dto) {
        // Step 1: Lead fetch karo
        LeadContact lead = leadContactRepository.findById(leadId)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + leadId));

        // Step 2: Company build karo
        CompanyDetails company = CompanyDetails.builder()
                .companyName(dto.getCompanyName())
                .website(dto.getWebsite())
                .mobileNumber(dto.getMobileNumber())
                .officePhoneNumber(dto.getOfficePhoneNumber())
                .country(dto.getCountry())
                .state(dto.getState())
                .city(dto.getCity())
                .postalCode(dto.getPostalCode())
                .address(dto.getAddress())
                .build();

        // Step 3: Save company
        CompanyDetails savedCompany = companyDetailsRepository.save(company);

        // Step 4: Lead ke andar company set karo
        lead.setCompanyDetails(savedCompany);
        leadContactRepository.save(lead);

        // Step 5: DTO return karo
        return CompanyDetailsMapper.toDTO(savedCompany);
    }
}