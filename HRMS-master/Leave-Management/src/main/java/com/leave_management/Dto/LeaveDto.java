package com.leave_management.Dto;

import com.leave_management.Enum.LeaveAction;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveDto {
    private Long id;
    private String leaveNo;
    @NotBlank(message = "Name is required")
    private String name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate toDate;

    private int total;       // lowercase 'total' for Java naming convention
    private String reason;
    private String attachment;
    private boolean status;
    @Enumerated(EnumType.STRING)  // Store enum as string in DB
    private LeaveAction action;
    private LocalDate createdAt;

    private Long userId;
    private String role;
}
