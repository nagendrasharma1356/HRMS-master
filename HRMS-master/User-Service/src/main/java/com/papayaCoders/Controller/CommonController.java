package com.papayaCoders.Controller;

import com.papayaCoders.Dto.MonthlyLeaveSalarySummaryDTO;
import com.papayaCoders.Dto.UserDto;
import com.papayaCoders.Repository.EmployeeRepository;
import com.papayaCoders.Repository.HrRepository;
import com.papayaCoders.Repository.MangerRepository;
import com.papayaCoders.Service.CommonService;
import com.papayaCoders.common.ApiResponse;
import com.papayaCoders.model.Employee;
import com.papayaCoders.model.Hr;
import com.papayaCoders.model.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/common")
public class CommonController
{
    @Autowired
    private HrRepository hrRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MangerRepository  mangerRepository;

    @Autowired
    private CommonService commonService;



    @GetMapping("/users/{role}/{id}")
    public ResponseEntity<UserDto> getUserByRoleAndId(
            @PathVariable String role,
            @PathVariable Long id) {

        switch (role.toUpperCase()) {
            case "HR": {
                Optional<Hr> hrOpt = hrRepository.findById(id);
                if (hrOpt.isPresent()) {
                    Hr hr = hrOpt.get();
                    return ResponseEntity.ok(new UserDto(hr.getId(), hr.getName(), hr.getEmail(),"HR",hr.getSalary()));
                }
                break;
            }

            case "EMPLOYEE": {
                Optional<Employee> empOpt = employeeRepository.findById(id);
                if (empOpt.isPresent()) {
                    Employee emp = empOpt.get();
                    return ResponseEntity.ok(new UserDto(emp.getId(), emp.getName(), emp.getEmail(), "EMPLOYEE",emp.getSalary()));
                }
                break;
            }

            case "MANAGER": {
                Optional<Manager> manOpt = mangerRepository.findById(id);
                if (manOpt.isPresent()) {
                    Manager man = manOpt.get();
                    return ResponseEntity.ok(new UserDto(man.getId(), man.getName(), man.getEmail(), "MANAGER",man.getSalary()));
                }
                break;
            }

            default:
                return ResponseEntity.badRequest().body(null); // Invalid role
        }

        return ResponseEntity.notFound().build();
    }


    @PostMapping("/deductSalary")
    public ResponseEntity<ApiResponse> deductSalary(
            @RequestParam Long userId,
            @RequestParam String role) {

        ApiResponse response = commonService.deductSalaryBasedOnPaidLeave(userId, role);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
                .body(response);
    }



    @GetMapping("/monthlySummary")
    public ResponseEntity<ApiResponse> getMonthlyLeaveSalarySummary(
            @RequestParam Long userId,
            @RequestParam String role) {
        try {
            MonthlyLeaveSalarySummaryDTO summary = commonService.getMonthlyLeaveSalarySummary(userId, role);
            return ResponseEntity.ok(new ApiResponse(true, "Monthly leave and salary summary fetched", summary));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Failed to get summary: " + e.getMessage()));
        }
    }

}
