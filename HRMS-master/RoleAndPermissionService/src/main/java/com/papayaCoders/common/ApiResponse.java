package com.papayaCoders.common;

import lombok.Data;

@Data

public class ApiResponse<T>
{
    private String message;
    private boolean success;
    private T data;

    public ApiResponse() {

    }


    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Optionally include this for convenience:
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data = null;
    }
    public ApiResponse<T> withDefaultErrorMessage(String defaultMessage) {
        if (!this.success && (this.message == null || this.message.isBlank())) {
            this.message = defaultMessage;
        }
        return this;
    }

}
