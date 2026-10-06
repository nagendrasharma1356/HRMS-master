package com.example.Client.Service.repository;

import com.example.Client.Service.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    @Query("SELECT c FROM Company c WHERE c.Company = :company")
    Optional<Company> findByCompany(@Param("company") String company);

}
