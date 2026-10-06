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
public class HolidayRequestDto
{
    @NotNull(message = "date can't be empty")
    private Date date;

    @NotBlank(message = "Title can't be empty")
    private String title;

    @NotBlank(message = "Description can't be empty")
    private String Description;
}
