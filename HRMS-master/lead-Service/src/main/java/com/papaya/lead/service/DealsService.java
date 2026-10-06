package com.papaya.lead.service;

import com.papaya.lead.config.PageResponse;
import com.papaya.lead.dto.DealsDTO;


import java.util.List;
public interface DealsService {
    DealsDTO createDeal(Long leadContactId, DealsDTO dto);
    DealsDTO getDealById(Long id);
    List<DealsDTO> getAllDeals();
    List<DealsDTO> getDealsByLeadContactId(Long leadContactId);
    DealsDTO updateDeal(Long id, DealsDTO dto);
    void deleteDeal(Long id);
    PageResponse<DealsDTO> getAllDealsPaginated(int page, int size);
    List<DealsDTO> getDealsByCompanyId(Long companyDetailsId);// new

}