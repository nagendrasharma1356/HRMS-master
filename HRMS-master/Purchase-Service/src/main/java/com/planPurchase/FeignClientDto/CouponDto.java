package com.planPurchase.FeignClientDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponDto
{
    private Long id;
    private String couponCode;
    private String couponName;
    private String description;
    private Double discount;
    private Double maxValue;
    private Double minValue;
    private String validTill;
    private Integer perUserLimit;
    private String createdAt;
    private String updatedAt;
}
