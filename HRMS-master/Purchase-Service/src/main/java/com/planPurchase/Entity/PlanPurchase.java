package com.planPurchase.Entity;

import com.planPurchase.Enum.PaymentStatus;
import com.planPurchase.Enum.PlanStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "plan_purchases")
public class PlanPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientId;

    private Long companyId;

    private Long planId;

    private String transactionId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private PlanStatus planStatus = PlanStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();


    private String paymentMode;
    private Long couponId;
    private Double couponDiscount;
    private Double companyDiscount;
    private String remarks;
    private Double totalAmount;

}