package com.plan.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompanyServiceDto {
    private Long id;
    private String serviceName;
    private Float price;
    private Integer companyId;
    private Integer discount;
    private Double finalPrice;
    private Boolean isActive = true;
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();
}
