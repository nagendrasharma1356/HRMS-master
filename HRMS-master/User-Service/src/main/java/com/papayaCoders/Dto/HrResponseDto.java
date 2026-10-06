package com.papayaCoders.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HrResponseDto
{
    private Long id;

    private String name;

    private String email;

    private String phone;

    private String department;

    private String position;
    private Double salary;
    private String role;
    private LocalDate createdAt;

    private LocalDate updatedAt;
}
