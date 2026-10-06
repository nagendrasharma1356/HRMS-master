package com.papayaCoders.model;
// admin, manager,hr, employee

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Hr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotBlank(message = "phone is required")
    private String phone;

    @NotBlank(message = "department is required")
    private String department;

    @NotBlank(message = "position is required")
    private String position;

    @NotBlank(message = "password is required")
    private String password;

    private Double salary;

    private String role= "ROLE_HR";

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private boolean status = true;
}
