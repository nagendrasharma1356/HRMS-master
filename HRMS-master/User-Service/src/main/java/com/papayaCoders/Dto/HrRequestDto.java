package com.papayaCoders.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HrRequestDto
{
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

}
