package com.papaya.lead.service;

import com.papaya.lead.config.PageResponse;
import com.papaya.lead.dto.LeadContactDTO;

import java.util.List;

public interface LeadContactService {
    LeadContactDTO createLeadContact(LeadContactDTO dto);
    LeadContactDTO getLeadContactById(Long id);
    List<LeadContactDTO> getAllLeadContacts();
    LeadContactDTO updateLeadContact(Long id, LeadContactDTO dto);
    void deleteLeadContact(Long id);
    PageResponse<LeadContactDTO> getAllLeadsPaginated(int page, int size);
}