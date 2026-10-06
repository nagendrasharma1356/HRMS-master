package com.HolidayService.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HolidayResponseDto
{
    private Long id;
    private Date date;
    private String title;
    private String Description;
    private String action;
}
