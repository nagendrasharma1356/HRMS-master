package com.papaya.lead.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "lead_contacts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String salutation;
    private String name;

    private String email;

    private String leadSource;
    private String addedBy;
    private String leadOwner;

    // One LeadContact can have multiple Deals
    @OneToMany(mappedBy = "leadContact", cascade = CascadeType.ALL)
    private List<Deals> deals;

    // Many LeadContact has one CompanyDetails
    @ManyToOne
    @JoinColumn(name = "company_details_id", nullable = true)
    private CompanyDetails companyDetails;

}