package com.papayaCoders.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyLeaveSalarySummaryDTO {
    private Long userId;
    private String role;
    private int totalAllocatedLeave;
    private int paidLeavesTaken;
    private int totalLeaveTaken;
    private double salaryDeduction;
    private double baseSalary;
    private double netSalary;

}
