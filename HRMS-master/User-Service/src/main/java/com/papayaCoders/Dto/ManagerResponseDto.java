package com.papayaCoders.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerResponseDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String department;
    private String role ;
    private String createdAt;
    private String updatedAt;
    private Double salary;
}
