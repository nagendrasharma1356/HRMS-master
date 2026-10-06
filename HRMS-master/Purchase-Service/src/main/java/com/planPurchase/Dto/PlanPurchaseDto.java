package com.planPurchase.Dto;

import com.planPurchase.Enum.PaymentStatus;
import com.planPurchase.Enum.PlanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanPurchaseDto
{
    private Long id;
    private Long clientId;
    private Long companyId;
    private Long planId;
    private String transactionId;
    private PaymentStatus paymentStatus ;
    private PlanStatus planStatus ;
    private LocalDateTime createdAt ;
    private LocalDateTime updatedAt ;
    private String paymentMode;
    private String remarks;
    private Long couponId;
    private Double couponDiscount;
    private Double companyDiscount;
    private Double totalAmount;

}
