package com.papaya.lead.mapper;

import com.papaya.lead.dto.CompanyDetailsDTO;
import com.papaya.lead.entity.CompanyDetails;

public class CompanyDetailsMapper {

    public static CompanyDetailsDTO toDTO(CompanyDetails entity) {
        return CompanyDetailsDTO.builder()
                .id(entity.getId())
                .companyName(entity.getCompanyName())
                .website(entity.getWebsite())
                .mobileNumber(entity.getMobileNumber())
                .officePhoneNumber(entity.getOfficePhoneNumber())
                .country(entity.getCountry())
                .state(entity.getState())
                .city(entity.getCity())
                .postalCode(entity.getPostalCode())
                .address(entity.getAddress())
                .build();
    }

    public static CompanyDetails toEntity(CompanyDetailsDTO dto) {
        return CompanyDetails.builder()
                .id(dto.getId())
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
    }
}
