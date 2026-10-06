package com.papaya.lead.repository;

import com.papaya.lead.entity.Deals;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DealsRepository extends JpaRepository<Deals, Long> {
    List<Deals> findByLeadContactId(Long leadContactId);
    List<Deals> findByCompanyDetailsId(Long companyDetailsId);

}