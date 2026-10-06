package com.papaya.lead.dto;

import com.papaya.lead.dto.CompanyDetailsDTO;
import com.papaya.lead.dto.DealsDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadContactDTO {

    private Long id;
    private String salutation;
    private String name;
    private String email;
    private String leadSource;
    private String addedBy;
    private String leadOwner;

    private CompanyDetailsDTO companyDetails;
    private List<DealsDTO> deals;
}