package com.leave_management.exception;

public class ResourceNotFoundException extends RuntimeException
{
    public ResourceNotFoundException(String message){
        super(message);
    }

    public ResourceNotFoundException(String category, String id, Long categoryId) {
    }
}
