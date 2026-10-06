package com.coupon.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
