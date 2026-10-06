package com.expense.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExpenseCategoryDto
{
    private Long id;
    private String name;
    private String Description;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
