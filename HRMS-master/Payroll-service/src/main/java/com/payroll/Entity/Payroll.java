package com.payroll.Entity;

import com.payroll.enumClass.PayrollType;
import com.payroll.enumClass.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Type is required")
    @Enumerated(EnumType.STRING)
    private PayrollType type;



    @NotBlank(message ="Name is required" )
    private String name;
    @NotNull(message = "Amount is required")
    private Double fixedAmount;
    private String action;
    private int percentage;

    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    @Transient
    private Long userId;
    @Transient
    private String userRole;


}
