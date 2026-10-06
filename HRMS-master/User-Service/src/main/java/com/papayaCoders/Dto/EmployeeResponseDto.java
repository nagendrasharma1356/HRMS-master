package com.papayaCoders.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponseDto
{
    private Long id;
    private String name;

    private String email;

    private String phone;

    private String department;

    private String position;

    private Double salary;

    private LocalDate dateOfJoining;


    private String address;

    private String gender;

    private LocalDate dateOfBirth;

    private String reportingManager;

    private String bloodGroup;

    private String nationality;

    private String panNumber;

    private Long aadhaarNumber;

    private String maritalStatus;
    private LocalDate createdAt;

    private LocalDate updatedAt;
}
