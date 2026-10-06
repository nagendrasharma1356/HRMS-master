package com.papayaCoders.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequestDto
{
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "Phone no is required")
    private String phone;

    @NotBlank(message = "Department is required")
    private String department;

    private String position;

    @NotNull(message = "salary is required")

    private Double salary;

    @NotNull(message = "Joining Date is required")
    private LocalDate dateOfJoining;

    private boolean status=true;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Gender is required")
    private String gender;

    private LocalDate dateOfBirth;

    private String reportingManager;

    private String bloodGroup;

    @NotBlank(message = "Nationality is required")
    private String nationality;

    private String panNumber;

    @NotNull(message = "Aadhaar Number  is required")
    private Long aadhaarNumber;

    private String maritalStatus;



    @NotBlank(message = "Password is required")
    private String password;
}
