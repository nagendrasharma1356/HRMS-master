package com.payroll.exception;

public class PayrollAlreadyAssignedException extends RuntimeException {
    public PayrollAlreadyAssignedException(String message) {
        super(message);
    }
}

