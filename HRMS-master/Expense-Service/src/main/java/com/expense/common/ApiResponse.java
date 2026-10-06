package com.expense.common;

import com.expense.dto.ExpenseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;


    public ApiResponse(String expenseCreatedSuccessfully, boolean b, ExpenseDto created) {
    }

    public ApiResponse() {

    }

}
