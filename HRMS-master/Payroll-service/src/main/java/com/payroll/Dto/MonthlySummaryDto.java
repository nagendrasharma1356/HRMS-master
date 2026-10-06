package com.payroll.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MonthlySummaryDto {
    private Long userId;
    private String role;
    private int totalAllocatedLeave;
    private int paidLeavesTaken;
    private Integer totalLeaveTaken;
    private Double allowances;
    private Double deductions;
    private Double baseSalary;
    private Double salaryDeduction;
    private Double netSalary;


    // getters and setters
}
