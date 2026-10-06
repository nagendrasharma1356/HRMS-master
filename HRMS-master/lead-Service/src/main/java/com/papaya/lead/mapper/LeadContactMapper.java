package com.papaya.lead.mapper;

import com.papaya.lead.dto.LeadContactDTO;
import com.papaya.lead.entity.LeadContact;

import java.util.stream.Collectors;

public class LeadContactMapper {

    public static LeadContactDTO toDTO(LeadContact entity) {
        if (entity == null) return null;

        return LeadContactDTO.builder()
                .id(entity.getId())
                .salutation(entity.getSalutation())
                .name(entity.getName())
                .email(entity.getEmail())
                .leadSource(entity.getLeadSource())
                .addedBy(entity.getAddedBy())
                .leadOwner(entity.getLeadOwner())
                .companyDetails(entity.getCompanyDetails() != null
                        ? CompanyDetailsMapper.toDTO(entity.getCompanyDetails())
                        : null)
                .deals(entity.getDeals() != null ?
                        entity.getDeals().stream()
                                .map(DealsMapper::toDTO)
                                .collect(Collectors.toList()) : null)
                .build();
    }


    public static LeadContact toEntity(LeadContactDTO dto) {
        if (dto == null) {
            return null;
        }
        LeadContact lead = LeadContact.builder()
                .id(dto.getId())
                .salutation(dto.getSalutation())
                .name(dto.getName())
                .email(dto.getEmail())
                .leadSource(dto.getLeadSource())
                .addedBy(dto.getAddedBy())
                .leadOwner(dto.getLeadOwner())
                // Don't set companyDetails here, set it in service after fetching from DB
                .build();
        return lead;
    }

}

