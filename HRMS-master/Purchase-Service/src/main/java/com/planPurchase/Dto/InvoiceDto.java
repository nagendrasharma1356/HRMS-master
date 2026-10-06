package com.planPurchase.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceDto
{


    private Long purchaseId;
    private Long clientId;
    private Long companyId;
    private Long planId;
    private String paymentMode;
    private Double totalAmount;
    private Double couponDiscount;
    private Double companyDiscount;

    private LocalDateTime invoiceDate;

}
