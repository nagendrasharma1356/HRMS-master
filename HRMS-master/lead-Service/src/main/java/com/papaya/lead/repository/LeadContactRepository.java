package com.papaya.lead.repository;

import com.papaya.lead.entity.LeadContact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadContactRepository extends JpaRepository<LeadContact, Long> {
    LeadContact findByEmail(String email);
}