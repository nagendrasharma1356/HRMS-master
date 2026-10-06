package com.payroll.Dto;

import com.payroll.enumClass.PayrollType;
import com.payroll.enumClass.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PayrollDto {
    private Long id;
    private PayrollType type;
    @Enumerated(EnumType.STRING)
    private Status status;


    @NotBlank(message ="Name is required" )
    private String name;
    @NotNull(message = "Amount is required")
    private Double fixedAmount;
    private String action;
    private int percentage;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Long userId;

}
