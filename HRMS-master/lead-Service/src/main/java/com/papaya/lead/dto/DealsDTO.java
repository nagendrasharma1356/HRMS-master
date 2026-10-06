package com.papaya.lead.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealsDTO {

    private Long id;
    private String dealName;
    private String pipeline;
    private String dealStage;
    private BigDecimal dealValue;
    private LocalDate closedDate;
    private String dealCategory;
    private String dealAgent;
    private String product;
    private String dealWatcher;

    private Long leadContactId;
    private Long companyDetailsId; //Added for linking deal with company
}