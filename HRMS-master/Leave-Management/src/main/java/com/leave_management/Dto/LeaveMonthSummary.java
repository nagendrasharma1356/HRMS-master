package com.leave_management.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveMonthSummary
{
    private int no;
    private String month;
    private int allocatedCL;
    private long usedLWP;
    private long usedCL;
    private long totalUsed; // CL + LWP
    private int remainingCL;
}
