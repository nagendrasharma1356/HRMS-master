package com.planPurchase.FeignClientDto;

import lombok.Data;

@Data
public class CompanyServiceDto {

    private Long id;
    private String serviceName;
    private Float price;
    private Integer companyId;
    private Integer discount;
    private Double finalPrice;
}

