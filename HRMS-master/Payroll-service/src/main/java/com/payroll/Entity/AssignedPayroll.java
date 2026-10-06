package com.payroll.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "assigned_payrolls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignedPayroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long payrollId;
    private Long userId;
    private String userRole;
    private String payrollName;
    private Double allowances;
    private Double deductions;

    private LocalDate assignedAt;


}
