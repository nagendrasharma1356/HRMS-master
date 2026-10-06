package com.leave_management.service;




import com.leave_management.Dto.LeaveReportDTO;
import com.leave_management.Entity.Leave;
import com.leave_management.Enum.LeaveAction;
import com.leave_management.Repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaveReportService {

    @Autowired
    private LeaveRepository leaveRepository;

    private static final int MONTHLY_CL_ALLOCATED = 2;
    private static final int MONTHLY_TOTAL_ALLOCATED = 4;

    public List<LeaveReportDTO> generateMonthlyReport() {
        List<Leave> leaves = leaveRepository.findAll();

        Map<Month, List<Leave>> groupedByMonth = leaves.stream()
                .collect(Collectors.groupingBy(l -> l.getFromDate().getMonth()));

        List<LeaveReportDTO> reports = new ArrayList<>();
        int index = 1;

        for (Month month : Month.values()) {
            List<Leave> monthLeaves = groupedByMonth.getOrDefault(month, Collections.emptyList());

            int clUsed = monthLeaves.stream()
                    .filter(l -> l.getAction() == LeaveAction.CL)
                    .mapToInt(Leave::getTotal)
                    .sum();

            int lwpUsed = monthLeaves.stream()
                    .filter(l -> l.getAction() == LeaveAction.LWP)
                    .mapToInt(Leave::getTotal)
                    .sum();

            int totalUsed = clUsed + lwpUsed;
            int clRemaining = Math.max(MONTHLY_CL_ALLOCATED - clUsed, 0);
            int totalRemaining = Math.max(MONTHLY_TOTAL_ALLOCATED - totalUsed, 0);

            reports.add(new LeaveReportDTO(
                    index++,
                    month.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                    clUsed,
                    lwpUsed,
                    totalUsed,
                    clRemaining,
                    totalRemaining
            ));
        }

        return reports;
    }

    public Map<String, Object> calculatePaidLeavesFromRange(Long userId, String userRole) {
        String cleanUserRole = userRole.replace("\"", "").trim();

        System.out.println("Querying leaves for userId = " + userId + ", userRole = " + cleanUserRole);

        List<Leave> leaves = leaveRepository.findByUserIdAndUserRole(userId, cleanUserRole);

        // Filter only APPROVED leaves
        List<Leave> approvedLeaves = leaves.stream()
                .filter(leave -> leave.getAction() != null && leave.getAction().equals(LeaveAction.APPROVED))
                .toList();

        System.out.println("Total leaves found: " + leaves.size());
        System.out.println("Approved leaves found: " + approvedLeaves.size());

        Map<String, Integer> monthlyLeaveMap = new HashMap<>();
        Map<String, List<LocalDate>> monthlyLeaveDatesMap = new HashMap<>();

        for (Leave leave : approvedLeaves) {
            LocalDate start = leave.getFromDate();
            LocalDate end = leave.getToDate();

            while (!start.isAfter(end)) {
                String monthKey = start.getYear() + "-" + String.format("%02d", start.getMonthValue());
                monthlyLeaveMap.put(monthKey, monthlyLeaveMap.getOrDefault(monthKey, 0) + 1);

                monthlyLeaveDatesMap.computeIfAbsent(monthKey, k -> new ArrayList<>()).add(start);
                start = start.plusDays(1);
            }
        }

        int totalApproved = 0;
        int totalPaidLeaves = 0;
        List<String> paidLeaveDates = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : monthlyLeaveMap.entrySet()) {
            String monthKey = entry.getKey();
            int days = entry.getValue();
            totalApproved += days;

            if (days > MONTHLY_TOTAL_ALLOCATED) {
                int extraDays = days - MONTHLY_TOTAL_ALLOCATED;
                totalPaidLeaves += extraDays;

                List<LocalDate> leaveDates = monthlyLeaveDatesMap.get(monthKey);
                // Sort to ensure consistent ordering
                Collections.sort(leaveDates);
                List<LocalDate> paidDates = leaveDates.subList(MONTHLY_TOTAL_ALLOCATED, leaveDates.size());
                paidDates.forEach(date -> paidLeaveDates.add(date.toString()));
            }

            System.out.println("Month: " + monthKey + ", Leave Days: " + days);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("role", cleanUserRole);
        result.put("monthlyLimit", MONTHLY_TOTAL_ALLOCATED);
        result.put("totalApprovedLeaveDays", totalApproved);
        result.put("paidLeaves", totalPaidLeaves);
        result.put("monthlyLeaveBreakdown", monthlyLeaveMap);
        result.put("paidLeaveDates", paidLeaveDates); // ✅ ADD THIS

        return result;
    }


}
