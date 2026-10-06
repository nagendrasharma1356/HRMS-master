package com.papayaCoders.Service;

import com.papayaCoders.Dto.MonthlyLeaveSalarySummaryDTO;
import com.papayaCoders.FeignClientService.LeaveClient;
import com.papayaCoders.Repository.EmployeeRepository;
import com.papayaCoders.Repository.HrRepository;
import com.papayaCoders.Repository.MangerRepository;
import com.papayaCoders.Repository.SalaryDeductionRecordRepository;
import com.papayaCoders.common.ApiResponse;
import com.papayaCoders.model.Employee;
import com.papayaCoders.model.Hr;
import com.papayaCoders.model.Manager;
import com.papayaCoders.model.SalaryDeductionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CommonService
{
    @Autowired
    private LeaveClient leaveServiceClient;

    @Autowired
    private HrRepository hrRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MangerRepository managerRepository;

    @Autowired
    private SalaryDeductionRecordRepository salaryDeductionRecordRepository;
    public ApiResponse deductSalaryBasedOnPaidLeave(Long userId, String role) {
        ApiResponse response = leaveServiceClient.getLeaveSummary(userId, role);

        if (!response.isSuccess()) {
            return new ApiResponse(false, "Failed to get leave summary from leave service", null);
        }

        Map<String, Object> summary = (Map<String, Object>) response.getData();
        List<String> paidLeaveDatesStr = (List<String>) summary.getOrDefault("paidLeaveDates", List.of());

        if (paidLeaveDatesStr.isEmpty()) {
            return new ApiResponse(true, "No paid leaves found for deduction", null);
        }

        String normalizedRole = role.trim().toUpperCase();
        double totalDeductionThisRun = 0;
        boolean anyDeductionDone = false;

        double originalSalary;
        switch (normalizedRole) {
            case "EMPLOYEE":
                Employee emp = employeeRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Employee not found"));
                originalSalary = emp.getSalary();
                break;
            case "MANAGER":
                Manager mgr = managerRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Manager not found"));
                originalSalary = mgr.getSalary();
                break;
            case "HR":
                Hr hr = hrRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("HR not found"));
                originalSalary = hr.getSalary();
                break;
            default:
                return new ApiResponse(false, "Invalid role", null);
        }

        // Fetch all previous deduction records for this user (except summary record with leaveDate == null)
        List<SalaryDeductionRecord> previousDeductions = salaryDeductionRecordRepository.findAllByUserIdAndLeaveDateIsNotNull(userId);
        double totalPreviousDeductions = previousDeductions.stream()
                .mapToDouble(SalaryDeductionRecord::getDeductedAmount)
                .sum();

        for (String dateStr : paidLeaveDatesStr) {
            LocalDate leaveDate = LocalDate.parse(dateStr);

            if (salaryDeductionRecordRepository.existsByUserIdAndLeaveDate(userId, leaveDate)) {
                continue;
            }

            double dailySalary = originalSalary / 30.0;
            double deductionForThisDate = switch (normalizedRole) {
                case "EMPLOYEE" -> dailySalary;
                case "MANAGER" -> dailySalary * 0.5;
                default -> 0;
            };

            if (deductionForThisDate > 0) {
                SalaryDeductionRecord record = new SalaryDeductionRecord();
                record.setUserId(userId);
                record.setDeductedAmount(deductionForThisDate);
                record.setLeaveDate(leaveDate);
                record.setDeductDate(LocalDate.now());
                salaryDeductionRecordRepository.save(record);

                totalDeductionThisRun += deductionForThisDate;
                anyDeductionDone = true;
            }
        }

        double totalDeduction = totalPreviousDeductions + totalDeductionThisRun;
        double netSalary = originalSalary - totalDeduction;

        // Save or update summary record (leaveDate == null)
        Optional<SalaryDeductionRecord> optionalSummary = salaryDeductionRecordRepository.findNetSalaryRecordByUserId(userId);
        SalaryDeductionRecord summaryRecord = optionalSummary.orElseGet(SalaryDeductionRecord::new);
        summaryRecord.setUserId(userId);
        summaryRecord.setDeductedAmount(totalDeduction);
        summaryRecord.setNetSalary(netSalary);
        summaryRecord.setLeaveDate(null);
        salaryDeductionRecordRepository.save(summaryRecord);

        // Prepare data map including originalSalary
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("originalSalary", originalSalary);
        responseData.put("totalDeducted", totalDeduction);
        responseData.put("netSalary", netSalary);
        responseData.put("deductedDate",LocalDate.now());

        if (anyDeductionDone) {
            return new ApiResponse(true, "Salary deduction completed successfully", responseData);
        } else {
            return new ApiResponse(true, "Salary already deducted for all paid leaves", responseData);
        }
    }


    public MonthlyLeaveSalarySummaryDTO getMonthlyLeaveSalarySummary(Long userId, String role) {
        ApiResponse<Map<String, Object>> response = leaveServiceClient.getLeaveSummary(userId, role);

        if (!response.isSuccess()) {
            throw new RuntimeException("Failed to get leave summary from Leave Service");
        }

        Map<String, Object> summaryMap = response.getData();

        int monthlyLimit = (int) summaryMap.getOrDefault("monthlyLimit", 0);
        int paidLeavesTaken = (int) summaryMap.getOrDefault("paidLeaves", 0);
        int totalLeaveTaken = (int) summaryMap.getOrDefault("totalApprovedLeaveDays", 0);

        // Use monthlyLimit as allocated leaves (allowed leaves per month)
        int allocatedLeaves = monthlyLimit;

        // Calculate salary deduction based on paid leaves
        double salaryDeduction = calculateSalaryDeduction(userId, role, paidLeavesTaken);

        // Get base salary from user repo based on role
        double baseSalary = 0;
        switch(role.toLowerCase()) {
            case "employee":
                Employee emp = employeeRepository.findById(userId).orElseThrow(() -> new RuntimeException("Employee not found"));
                baseSalary = emp.getSalary();
                break;
            case "manager":
                Manager mgr = managerRepository.findById(userId).orElseThrow(() -> new RuntimeException("Manager not found"));
                baseSalary = mgr.getSalary();
                break;
            case "hr":
                Hr hr = hrRepository.findById(userId).orElseThrow(() -> new RuntimeException("HR not found"));
                baseSalary = hr.getSalary();
                break;
            default:
                throw new RuntimeException("Invalid role");
        }

        // Calculate net salary (assuming no additional allowances or deductions)
        double netSalary = baseSalary - salaryDeduction;

        return new MonthlyLeaveSalarySummaryDTO(
                userId,
                role,
                allocatedLeaves,
                paidLeavesTaken,
                totalLeaveTaken,
                salaryDeduction,
                baseSalary,
                netSalary // you need to add this parameter to your DTO constructor
        );
    }


    private double calculateSalaryDeduction(Long userId, String role, int paidLeavesTaken) {
        double deduction = 0;

        switch(role.toLowerCase()) {
            case "employee":
                Employee emp = employeeRepository.findById(userId).orElseThrow(() -> new RuntimeException("Employee not found"));
                double empDailySalary = emp.getSalary() / 30.0;
                deduction = paidLeavesTaken * empDailySalary;
                break;
            case "manager":
                Manager mgr = managerRepository.findById(userId).orElseThrow(() -> new RuntimeException("Manager not found"));
                double mgrDailySalary = mgr.getSalary() / 30.0;
                deduction = paidLeavesTaken * mgrDailySalary * 0.5;
                break;
            case "hr":
                // HR no deduction assumed
                deduction = 0;
                break;
            default:
                throw new RuntimeException("Invalid role");
        }
        return deduction;
    }



}
