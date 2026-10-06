package com.payroll.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignPayrollRequest
{
    private Long payrollId;
    private Long userId;
    private String role;
}
