package com.papayaCoders.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto
{
    private Long id;
    private String name;
    @NotBlank(message = "Email is required !!")
    private String email;
    private Long phone;
    @NotBlank(message = "Password is required !!")
    private String password;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
