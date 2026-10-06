package com.coupon.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CouponDto {
    private Long id ;

    private String couponCode;
    private  String couponName;

    private String description;

    private Integer discount;

    private Double maxValue;

    private Double minValue;

    private LocalDate validTill;

    private Integer perUserLimit;

    private LocalDate createdAt;

    private LocalDate updatedAt;
}
