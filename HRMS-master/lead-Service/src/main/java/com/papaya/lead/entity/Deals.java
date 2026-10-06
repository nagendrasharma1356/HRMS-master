package com.papaya.lead.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "deals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @JoinColumn(name = "lead_contact_id")
    private LeadContact leadContact;

    // New: ManyToOne relation with CompanyDetails
    @ManyToOne
    @JoinColumn(name = "company_details_id")
    private CompanyDetails companyDetails;

}