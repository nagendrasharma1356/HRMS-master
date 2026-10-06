package com.expense.dto;
import lombok.*;

import java.time.LocalDate;
import java.time.Year;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDto {
    private Long id;
    private String title;
    private Integer referenceNo;
    private Double amount;
    private Date date;
    private String description;
    private Year sessionYear;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Long categoryId;
    private String categoryName;
}