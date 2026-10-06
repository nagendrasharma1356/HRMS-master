package com.planPurchase.Service;

import com.planPurchase.Dto.InvoiceDto;
import com.planPurchase.Entity.PlanPurchase;
import com.planPurchase.Repository.PlanPurchaseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService 
{
    
    @Autowired
    private ModelMapper mapper;



    @Autowired
    private PlanPurchaseRepository purchaseRepository;

    // create invoice
    public InvoiceDto getInvoiceByPurchaseId(Long purchaseId) {

        PlanPurchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new RuntimeException("Purchase not found with id: " + purchaseId));

        InvoiceDto invoice = new InvoiceDto();
        invoice.setPurchaseId(purchase.getId());
        invoice.setClientId(purchase.getClientId());
        invoice.setCompanyId(purchase.getCompanyId());
        invoice.setPlanId(purchase.getPlanId());
        invoice.setPaymentMode(purchase.getPaymentMode());
        invoice.setCompanyDiscount(purchase.getCompanyDiscount());
        invoice.setInvoiceDate(java.time.LocalDateTime.now());
        invoice.setCouponDiscount(purchase.getCouponDiscount());
        invoice.setTotalAmount(purchase.getTotalAmount());

        // Just return details without saving
        return mapper.map(invoice, InvoiceDto.class);
    }





}
