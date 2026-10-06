package com.leave_management.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeaveReportResponse {
    private Long staffId;
    private int year;
    private List<LeaveMonthSummary> monthlyReport;
}
