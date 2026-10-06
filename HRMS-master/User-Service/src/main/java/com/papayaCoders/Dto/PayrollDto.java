package com.papayaCoders.Dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class PayrollDto
{
    private Long id;
    private String name;
    private String type;
    private String action;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String status;
    private Long userId;
}
