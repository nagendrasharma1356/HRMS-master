package com.papaya.lead.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDetailsDTO {

    private Long id;
    private String companyName;
    private String website;
    private String mobileNumber;
    private String officePhoneNumber;
    private String country;
    private String state;
    private String city;
    private String postalCode;
    private String address;
}