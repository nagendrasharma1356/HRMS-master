package com.plan.Repository;

import com.plan.Entity.CompanyService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyServiceRepository extends JpaRepository<CompanyService, Long> {

    boolean existsByServiceNameAndCompanyId(String serviceName, Long companyId);
}
