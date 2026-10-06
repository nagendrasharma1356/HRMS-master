package com.papaya.lead.service.impl;

import com.papaya.lead.config.PageResponse;
import com.papaya.lead.dto.DealsDTO;
import com.papaya.lead.entity.Deals;
import com.papaya.lead.entity.LeadContact;
import com.papaya.lead.entity.CompanyDetails;
import com.papaya.lead.exception.ResourceNotFoundException;
import com.papaya.lead.mapper.DealsMapper;
import com.papaya.lead.repository.CompanyDetailsRepository;
import com.papaya.lead.repository.DealsRepository;
import com.papaya.lead.repository.LeadContactRepository;
import com.papaya.lead.service.DealsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealsServiceImpl implements DealsService {

    private final DealsRepository dealsRepository;
    private final LeadContactRepository leadContactRepository;
    private final CompanyDetailsRepository companyDetailsRepository;

    @Override
    public DealsDTO createDeal(Long leadContactId, DealsDTO dto) {
        LeadContact leadContact = leadContactRepository.findById(leadContactId)
                .orElseThrow(() -> new ResourceNotFoundException("LeadContact not found with ID: " + leadContactId));

        Deals deal = DealsMapper.toEntity(dto, leadContact, null);
        return DealsMapper.toDTO(dealsRepository.save(deal));
    }

    @Override
    public DealsDTO getDealById(Long id) {
        Deals deal = dealsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deal not found with ID: " + id));
        return DealsMapper.toDTO(deal);
    }

    @Override
    public List<DealsDTO> getDealsByCompanyId(Long companyDetailsId) {
        CompanyDetails company = companyDetailsRepository.findById(companyDetailsId)
                .orElseThrow(() -> new ResourceNotFoundException("CompanyDetails not found with ID: " + companyDetailsId));

        return dealsRepository.findByCompanyDetailsId(companyDetailsId)
                .stream()
                .map(DealsMapper::toDTO)
                .toList();
    }

    @Override
    public List<DealsDTO> getAllDeals() {
        return dealsRepository.findAll()
                .stream()
                .map(DealsMapper::toDTO)
                .toList();
    }

    @Override
    public List<DealsDTO> getDealsByLeadContactId(Long leadContactId) {
        LeadContact lead = leadContactRepository.findById(leadContactId)
                .orElseThrow(() -> new ResourceNotFoundException("LeadContact not found with ID: " + leadContactId));

        return dealsRepository.findByLeadContactId(leadContactId)
                .stream()
                .map(DealsMapper::toDTO)
                .toList();
    }

    @Override
    public DealsDTO updateDeal(Long id, DealsDTO dto) {
        Deals deal = dealsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deal not found with ID: " + id));

        deal.setDealName(dto.getDealName());
        deal.setPipeline(dto.getPipeline());
        deal.setDealStage(dto.getDealStage());
        deal.setDealValue(dto.getDealValue());
        deal.setClosedDate(dto.getClosedDate());
        deal.setDealCategory(dto.getDealCategory());
        deal.setDealAgent(dto.getDealAgent());
        deal.setProduct(dto.getProduct());
        deal.setDealWatcher(dto.getDealWatcher());

        return DealsMapper.toDTO(dealsRepository.save(deal));
    }

    @Override
    public void deleteDeal(Long id) {
        Deals deal = dealsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deal not found with ID: " + id));

        dealsRepository.delete(deal);
    }

    @Override
    public PageResponse<DealsDTO> getAllDealsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Deals> dealsPage = dealsRepository.findAll(pageable);

        List<DealsDTO> content = dealsPage.getContent()
                .stream()
                .map(DealsMapper::toDTO)
                .toList();

        return PageResponse.<DealsDTO>builder()
                .content(content)
                .pageNumber(dealsPage.getNumber())
                .pageSize(dealsPage.getSize())
                .totalElements(dealsPage.getTotalElements())
                .totalPages(dealsPage.getTotalPages())
                .lastPage(dealsPage.isLast())
                .build();
    }
}