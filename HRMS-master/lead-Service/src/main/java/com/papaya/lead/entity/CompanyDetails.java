package com.papaya.lead.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "company_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String website;

    @Column(length = 15)
    private String mobileNumber;

    @Column(length = 15)
    private String officePhoneNumber;

    private String country;
    private String state;
    private String city;

    @Column(length = 10)
    private String postalCode;

    private String address;

    // CompanyDetails → OneToMany Deals
    @OneToMany(mappedBy = "companyDetails")
    private List<Deals> deals;
}