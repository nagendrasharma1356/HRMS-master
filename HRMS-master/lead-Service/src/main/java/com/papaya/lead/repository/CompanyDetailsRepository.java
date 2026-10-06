package com.papaya.lead.repository;

import com.papaya.lead.entity.CompanyDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyDetailsRepository extends JpaRepository<CompanyDetails, Long> {
    CompanyDetails findByCompanyName(String companyName);
}