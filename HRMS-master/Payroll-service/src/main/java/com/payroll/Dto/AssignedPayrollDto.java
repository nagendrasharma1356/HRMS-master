package com.payroll.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignedPayrollDto {
    private Long id;
    private Long payrollId;
    private String payrollName;
    private Long userId;
    private String userRole;
    private String userName;
    private Double basicSalary;
    private LocalDate assignedAt;
}

