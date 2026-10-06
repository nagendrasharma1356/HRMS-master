package com.papayaCoders.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "salary_deductions")
@AllArgsConstructor
    @NoArgsConstructor
@Data
public class SalaryDeductionRecord {


    @Id
    private Long userId;
    private Double deductedAmount;
    private LocalDate leaveDate;
    private Double netSalary;
    private LocalDate deductDate;
}