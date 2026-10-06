package com.papaya.lead.service.impl;

import com.papaya.lead.config.PageResponse;
import com.papaya.lead.dto.LeadContactDTO;
import com.papaya.lead.entity.CompanyDetails;
import com.papaya.lead.entity.LeadContact;
import com.papaya.lead.exception.ResourceNotFoundException;
import com.papaya.lead.mapper.LeadContactMapper;
import com.papaya.lead.repository.CompanyDetailsRepository;
import com.papaya.lead.repository.LeadContactRepository;
import com.papaya.lead.service.LeadContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeadContactServiceImpl implements LeadContactService {

    private final LeadContactRepository leadContactRepository;
    private final CompanyDetailsRepository companyDetailsRepository;

    @Override
    public LeadContactDTO createLeadContact(LeadContactDTO dto) {
        // Map all simple fields from DTO to entity (no company yet)
        LeadContact lead = LeadContactMapper.toEntity(dto);


        // If caller passed a companyDetails.id, fetch that company and set it.
        if (dto.getCompanyDetails() != null && dto.getCompanyDetails().getId() != null) {
            CompanyDetails company = companyDetailsRepository.findById(dto.getCompanyDetails().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Company not found with id: " + dto.getCompanyDetails().getId()
                            )
                    );
            lead.setCompanyDetails(company);
        }

        LeadContact saved = leadContactRepository.save(lead);
        return LeadContactMapper.toDTO(saved);
    }

    // create

    @Override
    public LeadContactDTO getLeadContactById(Long id) {
        return leadContactRepository.findById(id)
                .map(LeadContactMapper::toDTO)
                .orElseThrow(() ->
                        new ResourceNotFoundException("LeadContact not found with Id: " + id)
                );
    }

    @Override
    public List<LeadContactDTO> getAllLeadContacts() {
        return leadContactRepository.findAll()
                .stream()
                .map(LeadContactMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LeadContactDTO updateLeadContact(Long id, LeadContactDTO dto) {
        LeadContact existing = leadContactRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("LeadContact not found with Id: " + id)
                );

        // Update simple fields
        existing.setSalutation(dto.getSalutation());
        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setLeadSource(dto.getLeadSource());
        existing.setAddedBy(dto.getAddedBy());
        existing.setLeadOwner(dto.getLeadOwner());

        // Handle optional companyDetails
        if (dto.getCompanyDetails() != null && dto.getCompanyDetails().getId() != null) {
            CompanyDetails company = companyDetailsRepository.findById(dto.getCompanyDetails().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Company not found with id: " + dto.getCompanyDetails().getId()
                            )
                    );
            existing.setCompanyDetails(company);
        } else {
            existing.setCompanyDetails(null);
        }

        LeadContact updated = leadContactRepository.save(existing);
        return LeadContactMapper.toDTO(updated);
    }

    @Override
    public void deleteLeadContact(Long id) {
        if (!leadContactRepository.existsById(id)) {
            throw new ResourceNotFoundException("LeadContact not found with Id: " + id);
        }
        leadContactRepository.deleteById(id);
    }

    @Override
    public PageResponse<LeadContactDTO> getAllLeadsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LeadContact> pageResult = leadContactRepository.findAll(pageable);

        List<LeadContactDTO> content = pageResult.getContent()
                .stream()
                .map(LeadContactMapper::toDTO)
                .collect(Collectors.toList());

        return PageResponse.<LeadContactDTO>builder()
                .content(content)
                .pageNumber(pageResult.getNumber())
                .pageSize(pageResult.getSize())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .lastPage(pageResult.isLast())
                .build();
    }
}