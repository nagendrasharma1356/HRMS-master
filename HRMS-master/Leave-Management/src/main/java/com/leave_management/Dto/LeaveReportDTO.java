package com.leave_management.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveReportDTO {
    private int no;
    private String month;
    private int clUsed;
    private int lwpUsed;
    private int totalUsed;
    private int clRemaining;
    private int totalRemaining;
}