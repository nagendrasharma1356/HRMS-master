package com.leave_management.common;

import com.leave_management.Dto.LeaveDto;
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


    public ApiResponse() {

    }
}
