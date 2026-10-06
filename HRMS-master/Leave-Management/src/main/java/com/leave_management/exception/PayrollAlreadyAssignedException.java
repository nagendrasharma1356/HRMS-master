package com.leave_management.exception;

public class PayrollAlreadyAssignedException extends RuntimeException {
    public PayrollAlreadyAssignedException(String message) {
        super(message);
    }
}

