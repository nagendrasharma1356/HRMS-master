package com.papaya.lead.mapper;

import com.papaya.lead.dto.DealsDTO;
import com.papaya.lead.entity.CompanyDetails;
import com.papaya.lead.entity.Deals;
import com.papaya.lead.entity.LeadContact;

public class DealsMapper {

    public static DealsDTO toDTO(Deals entity) {
        return DealsDTO.builder()
                .id(entity.getId())
                .dealName(entity.getDealName())
                .pipeline(entity.getPipeline())
                .dealStage(entity.getDealStage())
                .dealValue(entity.getDealValue())
                .closedDate(entity.getClosedDate())
                .dealCategory(entity.getDealCategory())
                .dealAgent(entity.getDealAgent())
                .product(entity.getProduct())
                .dealWatcher(entity.getDealWatcher())
                .leadContactId(entity.getLeadContact() != null ? entity.getLeadContact().getId() : null)
                .build();
    }

    public static Deals toEntity(DealsDTO dto, LeadContact leadContact, CompanyDetails companyDetails) {
        return Deals.builder()
                .id(dto.getId())
                .dealName(dto.getDealName())
                .pipeline(dto.getPipeline())
                .dealStage(dto.getDealStage())
                .dealValue(dto.getDealValue())
                .closedDate(dto.getClosedDate())
                .dealCategory(dto.getDealCategory())
                .dealAgent(dto.getDealAgent())
                .product(dto.getProduct())
                .dealWatcher(dto.getDealWatcher())
                .leadContact(leadContact)
                .build();
    }
}