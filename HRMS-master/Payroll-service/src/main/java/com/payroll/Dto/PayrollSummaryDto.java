package com.payroll.Dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayrollSummaryDto {
    private String name;
    private String status;
    private Double basicSalary;
    private Integer monthlyAllowedPaidLeaves;
    private Integer takenLeaves;
    private Double salaryDeduction;
    private Double allowances;
    private Double deductions;
    private Double netSalary;
    private String action;
}
