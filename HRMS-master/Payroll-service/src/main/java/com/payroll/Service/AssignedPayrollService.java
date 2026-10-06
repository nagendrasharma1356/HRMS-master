package com.payroll.Service;

import com.payroll.Dto.*;
import com.payroll.Entity.AssignedPayroll;
import com.payroll.Entity.Payroll;
import com.payroll.FeignClientService.UserClient;
import com.payroll.Repository.AssignedPayrollRepository;
import com.payroll.Repository.PayrollRepository;
import com.payroll.common.ApiResponse;
import com.payroll.common.PagedResponse;
import com.payroll.exception.PayrollAlreadyAssignedException;
import com.payroll.exception.ResourceNotFoundException;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AssignedPayrollService {
    @Autowired
    private AssignedPayrollRepository assignedPayrollRepository;
    @Autowired
    private UserClient userClient;

    @Autowired
    private PayrollRepository payrollRepository;
    @Autowired
    private ModelMapper mapper;


    // assigned payrolls
    public PayrollDto assignPayrollToUser(Long payrollId, Long userId, String role) {
        UserDto userDto;

        try {
            userDto = userClient.getUserByIdAndRole(role, userId);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("User not found in user-service with role: " + role);
        }

        Payroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll not found"));

        //  Check if payroll with the same name is already assigned
        if (assignedPayrollRepository.existsByPayrollName(payroll.getName())) {
            throw new PayrollAlreadyAssignedException("Payroll '" + payroll.getName() + "' is already assigned to a user.");
        }


        //  Save assignment
        AssignedPayroll assigned = AssignedPayroll.builder()
                .payrollId(payrollId)
                .payrollName(payroll.getName())
                .userId(userId)
                .userRole(role)
                .assignedAt(LocalDate.now())
                .build();
        assignedPayrollRepository.save(assigned);

        //  Update payroll (optional)
        payroll.setUserId(userId);
        payroll.setUserRole(userDto.getRole());
        payroll.setUpdatedAt(LocalDate.now());
        Payroll updated = payrollRepository.save(payroll);

        return mapper.map(updated, PayrollDto.class);
    }

    public AssignedPayrollDto getAssignedPayrollById(Long payrollId) {
        AssignedPayroll assigned = assignedPayrollRepository.findById(payrollId)
                .orElseThrow(() -> new ResourceNotFoundException("Assigned payroll not found with payrollId: " + payrollId));
        return mapper.map(assigned, AssignedPayrollDto.class);
    }


    public PagedResponse<AssignedPayrollDto> getAllAssignedPayrolls(Integer month, Integer year, Pageable pageable) {
        Page<AssignedPayroll> page = assignedPayrollRepository.findAllByMonthAndYear(month, year, pageable);

        List<AssignedPayrollDto> dtoList = page.getContent().stream().map(ap -> {
            AssignedPayrollDto dto = mapper.map(ap, AssignedPayrollDto.class);
            try {
                UserDto user = userClient.getUserByIdAndRole(ap.getUserRole(), ap.getUserId());
                dto.setUserName(user.getName());
                dto.setBasicSalary(user.getSalary());
            } catch (FeignException.NotFound e) {
                dto.setUserName("Unknown");
            }
            return dto;
        }).collect(Collectors.toList());

        return PagedResponse.<AssignedPayrollDto>builder()
                .content(dtoList)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    public void deleteAssignedPayroll(Long id) {
        AssignedPayroll assignedPayroll = assignedPayrollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assigned Payroll not found with id: " + id));

        assignedPayrollRepository.delete(assignedPayroll);
    }

    public ApiResponse<MonthlySummaryDto> getMonthlyLeaveSalarySummary(Long userId, String role) {
        try {
            // Sanitize role input
            role = role.replace("\"", "").trim().toUpperCase();
            System.out.println("Calling userClient with userId: " + userId + " and role: " + role);

            return userClient.getMonthlyLeaveSalarySummary(userId, role);

        } catch (FeignException e) {
            System.err.println("FeignException status: " + e.status());
            System.err.println("FeignException body: " + e.contentUTF8());

            if (e.status() == 400) {
                throw new IllegalArgumentException("Invalid role or bad request: " + e.contentUTF8());
            }

            throw new RuntimeException("Error while calling leave summary API", e);
        }
    }

    public PayrollSummaryDto getPayrollSummaryForUser(Long userId, String role) {
        // Step 1: Fetch user and validate
        UserDto userDto;
        try {
            userDto = userClient.getUserByIdAndRole(role, userId);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("User not found in user-service with role: " + role);
        }

        // Step 2: Fetch monthly summary
        ApiResponse<MonthlySummaryDto> apiResponse = getMonthlyLeaveSalarySummary(userId, role);
        MonthlySummaryDto summary = apiResponse.getData();

        if (summary == null) {
            throw new ResourceNotFoundException("Monthly summary not found for user: " + userId);
        }

        // Step 3: Fetch assigned payroll
        AssignedPayroll assignedPayroll = assignedPayrollRepository
                .findByUserIdAndUserRole(userId, role)
                .orElseThrow(() -> new ResourceNotFoundException("Assigned payroll not found for user"));

        // Step 4: Determine allowances and deductions to use
        double allowances = assignedPayroll.getAllowances() != null ? assignedPayroll.getAllowances()
                : (summary.getAllowances() != null ? summary.getAllowances() : 0.0);

        double deductions = assignedPayroll.getDeductions() != null ? assignedPayroll.getDeductions()
                : (summary.getDeductions() != null ? summary.getDeductions() : 0.0);

        // Base net salary calculation fallback
        double baseNetSalary = summary.getNetSalary() != null ? summary.getNetSalary()
                : (summary.getBaseSalary() != null ? summary.getBaseSalary() - (summary.getSalaryDeduction() != null ? summary.getSalaryDeduction() : 0.0) : 0.0);

        // Step 5: Calculate final net salary
        double finalNetSalary = baseNetSalary + allowances - deductions;

        // Step 6: Build and return DTO using assigned payroll's basicSalary
        return PayrollSummaryDto.builder()
                .name(assignedPayroll.getPayrollName())
                .status("ACTIVE")
                .basicSalary(summary.getBaseSalary() != null  ? summary.getBaseSalary() : 0.0)  // Use assigned payroll's basic salary here
                .monthlyAllowedPaidLeaves(summary.getPaidLeavesTaken() != 0 ? summary.getPaidLeavesTaken() : 0)
                .takenLeaves(summary.getTotalLeaveTaken() != null ? summary.getTotalLeaveTaken() : 0)
                .salaryDeduction(summary.getSalaryDeduction() != null ? summary.getSalaryDeduction() : 0.0)
                .allowances(allowances)
                .deductions(deductions)
                .netSalary(finalNetSalary)
                .action("View")
                .build();
    }




}
