package com.leave_management.Entity;

import com.leave_management.Enum.LeaveAction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leaves")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "leave_no", nullable = false, unique = true)
    private String leaveNo;  // Should be String if you want to search by String


    @NotBlank(message = "Name is required")
    private String name;

    private LocalDate fromDate;

    private LocalDate toDate;


    private int Total;

    @NotBlank(message = "Reason is required")
    private String reason;

    private String attachment;

    private boolean status;

    @Enumerated(EnumType.STRING)  // Store enum as string in DB
    private LeaveAction action;
    private LocalDate createdAt;

    private Long userId;
    private String userRole;

    @PrePersist // it automatic create
    public void prePersist() {
        this.createdAt = LocalDate.from(LocalDateTime.now());
    }

}
